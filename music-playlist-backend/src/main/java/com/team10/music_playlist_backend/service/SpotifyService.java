package com.team10.music_playlist_backend.service;

import com.team10.music_playlist_backend.dto.SongCharacteristicResponse;
import com.team10.music_playlist_backend.dto.SongDetailsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SpotifyService {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    private final WebClient webClient = WebClient.create();

    private String getAccessToken() {
        String basic = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8));
        Map<String, Object> res = webClient.post()
                .uri("https://accounts.spotify.com/api/token")
                .header("Authorization", "Basic " + basic)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("grant_type=client_credentials")
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        if (res == null || !res.containsKey("access_token")) throw new IllegalStateException("Spotify access token 발급 실패");
        return (String) res.get("access_token");
    }

    public SongDetailsResponse getSongDetails(String trackId) {
        String token = getAccessToken();
        Map<String, Object> track = webClient.get()
                .uri("https://api.spotify.com/v1/tracks/{id}?market=KR", trackId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        if (track == null) throw new IllegalStateException("트랙 정보를 가져오지 못했습니다.");
        Long id = (Long) track.get("id");
        String name = (String) track.get("name");
        String singer = "";
        Object artistsObj = track.get("artists");
        if (artistsObj instanceof List<?> artists) {
            singer = artists.stream().filter(Map.class::isInstance).map(a -> (String) ((Map<?, ?>) a).get("name")).reduce((a, b) -> a + ", " + b).orElse("");
        }
        String explanation = null;
        LocalDate releaseDate = null;
        Object albumObj = track.get("album");
        if (albumObj instanceof Map<?, ?> album) {
            explanation = (String) album.get("name");
            String rd = (String) album.get("release_date");
            releaseDate = parseReleaseDate(rd);
        }
        return SongDetailsResponse.builder().id(id).name(name).singer(singer).explanation(explanation).releaseDate(releaseDate).build();
    }

    public SongCharacteristicResponse getSongCharacteristic(String trackId) {
        String token = getAccessToken();
        Map<String, Object> feat = webClient.get()
                .uri("https://api.spotify.com/v1/audio-features/{id}", trackId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
        if (feat == null) throw new IllegalStateException("오디오 특성을 가져오지 못했습니다.");
        Integer bpm = null;
        Object tempoObj = feat.get("tempo");
        if (tempoObj instanceof Number n) bpm = Math.toIntExact(Math.round(n.doubleValue()));
        String keyStr = null;
        Object keyObj = feat.get("key");
        Object modeObj = feat.get("mode");
        if (keyObj instanceof Number k && modeObj instanceof Number md) keyStr = toKeyName(k.intValue(), md.intValue());
        return SongCharacteristicResponse.builder().bpm(bpm).key(keyStr).build();
    }

    public Map<String, Object> searchTrackRaw(String query, int limit) {
        String token = getAccessToken();
        String q = URLEncoder.encode(query, StandardCharsets.UTF_8);
        int lim = Math.max(1, Math.min(limit, 50));
        return webClient.get()
                .uri("https://api.spotify.com/v1/search?q=" + q + "&type=track&market=KR&limit=" + lim)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map<String, List<String>> getArtistsGenresBatch(Collection<String> artistIds) {
        if (artistIds == null || artistIds.isEmpty()) return Collections.emptyMap();
        String token = getAccessToken();
        Map<String, List<String>> result = new HashMap<>();
        List<String> ids = artistIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        for (int i = 0; i < ids.size(); i += 50) {
            List<String> chunk = ids.subList(i, Math.min(i + 50, ids.size()));
            String joined = String.join(",", chunk);
            Map<String, Object> res = webClient.get()
                    .uri("https://api.spotify.com/v1/artists?ids=" + joined)
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            Object artistsObj = res == null ? null : res.get("artists");
            if (artistsObj instanceof List<?> artists) {
                for (Object a : artists) {
                    if (a instanceof Map<?, ?> m) {
                        String id = (String) m.get("id");
                        Object genresObj = m.get("genres");
                        List<String> genres = new ArrayList<>();
                        if (genresObj instanceof List<?> gList) for (Object g : gList) if (g != null) genres.add(g.toString());
                        if (id != null) result.put(id, genres);
                    }
                }
            }
        }
        return result;
    }

    private LocalDate parseReleaseDate(String s) {
        if (s == null) return null;
        try {
            if (s.length() == 10) return LocalDate.parse(s);
            if (s.length() == 7) return LocalDate.parse(s + "-01");
            if (s.length() == 4) return LocalDate.parse(s + "-01-01");
        } catch (Exception ignored) {}
        return null;
    }

    private String toKeyName(int key, int mode) {
        String[] KEYS = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
        if (key < 0 || key > 11) return null;
        String base = KEYS[key];
        boolean isMinor = mode == 0;
        return isMinor ? base + "m" : base;
    }
}