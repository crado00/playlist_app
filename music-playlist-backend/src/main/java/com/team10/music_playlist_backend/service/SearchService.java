package com.team10.music_playlist_backend.service;

import com.team10.music_playlist_backend.dto.SearchAdvancedRequest;
import com.team10.music_playlist_backend.dto.SearchIntegrationRequest;
import com.team10.music_playlist_backend.dto.SearchItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SpotifyService spotifyService;

    public List<SearchItemResponse> integration(SearchIntegrationRequest req) {
        Map<String, Object> res = spotifyService.searchTrackRaw(req.getText(), 20);
        return mapTracksToItemsWithGenres(res, null);
    }

    public List<SearchItemResponse> advanced(SearchAdvancedRequest req) {
        Map<String, Object> res = spotifyService.searchTrackRaw(req.getText(), 30);
        String genreFilter = req.getGenre() == null ? null : req.getGenre().toLowerCase();
        return mapTracksToItemsWithGenres(res, genreFilter);
    }

    private List<SearchItemResponse> mapTracksToItemsWithGenres(Map<String, Object> res, String genreFilter) {
        if (res == null) return List.of();
        Object tracksObj = res.get("tracks");
        if (!(tracksObj instanceof Map<?, ?> tracks)) return List.of();
        Object itemsObj = tracks.get("items");
        if (!(itemsObj instanceof List<?> items)) return List.of();

        List<Map<String, Object>> rawItems = new ArrayList<>();
        Set<String> artistIdSet = new HashSet<>();

        for (Object it : items) {
            if (it instanceof Map<?, ?> m) {
                rawItems.add((Map<String, Object>) m);
                Object artistsObj = m.get("artists");
                if (artistsObj instanceof List<?> arts) {
                    for (Object a : arts) {
                        if (a instanceof Map<?, ?> am) {
                            String aid = (String) am.get("id");
                            if (aid != null) artistIdSet.add(aid);
                        }
                    }
                }
            }
        }

        Map<String, List<String>> genresByArtist = spotifyService.getArtistsGenresBatch(artistIdSet);

        List<SearchItemResponse> results = new ArrayList<>();
        for (Map<String, Object> m : rawItems) {
            String id = (String) m.get("id");
            String name = (String) m.get("name");
            String previewUrl = (String) m.get("preview_url");

            String singer = "";
            Object artistsObj = m.get("artists");
            List<String> artistIds = new ArrayList<>();
            if (artistsObj instanceof List<?> arts) {
                List<String> names = new ArrayList<>();
                for (Object a : arts) {
                    if (a instanceof Map<?, ?> am) {
                        names.add((String) am.get("name"));
                        String aid = (String) am.get("id");
                        if (aid != null) artistIds.add(aid);
                    }
                }
                singer = String.join(", ", names);
            }

            String album = null;
            LocalDate releaseDate = null;
            String imageUrl = null;
            Object albumObj = m.get("album");
            if (albumObj instanceof Map<?, ?> alb) {
                album = (String) alb.get("name");
                String rd = (String) alb.get("release_date");
                if (rd != null) {
                    try {
                        if (rd.length() == 10) releaseDate = LocalDate.parse(rd);
                        else if (rd.length() == 7) releaseDate = LocalDate.parse(rd + "-01");
                        else if (rd.length() == 4) releaseDate = LocalDate.parse(rd + "-01-01");
                    } catch (Exception ignored) {}
                }
                Object imgsObj = alb.get("images");
                if (imgsObj instanceof List<?> imgs && !imgs.isEmpty()) {
                    Object img0 = imgs.get(0);
                    if (img0 instanceof Map<?, ?> im) imageUrl = (String) im.get("url");
                }
            }

            List<String> genres = new ArrayList<>();
            for (String aid : artistIds) {
                List<String> gs = genresByArtist.getOrDefault(aid, List.of());
                for (String g : gs) if (!genres.contains(g)) genres.add(g);
            }

            if (genreFilter != null) {
                boolean anyMatch = genres.stream().map(String::toLowerCase).anyMatch(g -> g.equals(genreFilter) || g.contains(genreFilter));
                if (!anyMatch) continue;
            }

            results.add(SearchItemResponse.builder()
                    .id(id)
                    .name(name)
                    .singer(singer)
                    .album(album)
                    .releaseDate(releaseDate)
                    .imageUrl(imageUrl)
                    .previewUrl(previewUrl)
                    .genres(genres)
                    .build());
        }
        return results;
    }
}