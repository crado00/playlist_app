package com.team10.music_playlist_backend.service;

import com.team10.music_playlist_backend.dto.PlaylistEditRequest;
import com.team10.music_playlist_backend.dto.PlaylistRequest;
import com.team10.music_playlist_backend.dto.PlaylistResponse;
import com.team10.music_playlist_backend.entity.Music;
import com.team10.music_playlist_backend.entity.Playlist;
import com.team10.music_playlist_backend.entity.User;
import com.team10.music_playlist_backend.repository.MusicRepository;
import com.team10.music_playlist_backend.repository.PlaylistRepository;
import com.team10.music_playlist_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;
    private final SearchService searchService;

    public PlaylistResponse createPlaylist(Long userId, PlaylistRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        List<Music> musicList = request.getMusics().stream()
                .map(songDto -> musicRepository.findById(songDto.getId())
                        .orElseGet(() -> {
                            // DB에 없으면 새로 저장
                            Music newMusic = Music.builder()
                                    .id(songDto.getId())
                                    .title(songDto.getName())      // DTO의 이름 -> 엔티티 title
                                    .artist(songDto.getSinger())
                                    .build();
                            return musicRepository.save(newMusic);
                        })
                ).toList();

        Playlist playlist = Playlist.builder()
                .title(request.getTitle())
                .explanation(request.getExplanation())
                .imageUrl(request.getImageUrl())
                .musics(musicList)
                .user(user)
                .build();

        playlistRepository.save(playlist);
        return PlaylistResponse.fromEntity(playlist);
    }

    public Playlist getPlaylistById(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("플레이리스트를 찾을 수 없습니다."));
    }

    public List<PlaylistResponse> getUserPlaylists(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        return playlistRepository.findByUser(user)
                .stream()
                .map(PlaylistResponse::fromEntity)
                .toList();
    }

    public Playlist editPlaylist(Long playlistId, PlaylistEditRequest request, String username) {
        Playlist playlist = getPlaylistById(playlistId);
        if (!playlist.getUser().getUsername().equals(username)) {
            throw new RuntimeException("본인의 플레이리스트만 수정할 수 있습니다.");
        }
        List<Music> musicList = request.getMusics().stream()
                .map(songDto -> musicRepository.findById(songDto.getId())
                        .orElseGet(() -> {
                            // DB에 없으면 새로 저장
                            Music newMusic = Music.builder()
                                    .id(songDto.getId())
                                    .title(songDto.getTitle())      // DTO의 이름 -> 엔티티 title
                                    .artist(songDto.getArtist())
                                    .build();
                            return musicRepository.save(newMusic);
                        })
                ).toList();

        playlist.setTitle(request.getTitle());
        playlist.setExplanation(request.getExplanation());
        playlist.setMusics(request.getMusics());
        return playlistRepository.save(playlist);
    }

    public void deletePlaylist(Long playlistId, String username) {
        Playlist playlist = getPlaylistById(playlistId);
        if (!playlist.getUser().getUsername().equals(username)) {
            throw new RuntimeException("본인의 플레이리스트만 삭제할 수 있습니다.");
        }
        playlistRepository.delete(playlist);
    }

    // ✅ musicId를 String으로 변경
    public void removeMusicFromPlaylist(Long playlistId, String musicId, String username) {
        Playlist playlist = getPlaylistById(playlistId);
        if (!playlist.getUser().getUsername().equals(username)) {
            throw new RuntimeException("본인의 플레이리스트만 수정할 수 있습니다.");
        }

        playlist.getMusics().removeIf(music -> music.getId().equals(musicId));
        playlistRepository.save(playlist);
    }

    // ✅ orderedMusicIds를 List<String>으로 변경
    public Playlist reorderPlaylist(Long playlistId, List<String> orderedMusicIds, String username) {
        Playlist playlist = getPlaylistById(playlistId);
        if (!playlist.getUser().getUsername().equals(username)) {
            throw new RuntimeException("본인의 플레이리스트만 수정할 수 있습니다.");
        }

        List<Music> reordered = orderedMusicIds.stream()
                .map(id -> musicRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("음악을 찾을 수 없습니다.")))
                .toList();

        playlist.setMusics(reordered);
        return playlistRepository.save(playlist);
    }

    // ✅ musicId를 String으로 변경
    public Playlist addMusicToPlaylist(Long playlistId, String musicId, String username) {
        Playlist playlist = getPlaylistById(playlistId);
        if (!playlist.getUser().getUsername().equals(username)) {
            throw new RuntimeException("본인의 플레이리스트만 수정할 수 있습니다.");
        }

        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new RuntimeException("음악을 찾을 수 없습니다."));

        playlist.getMusics().add(music);
        return playlistRepository.save(playlist);
    }
}
