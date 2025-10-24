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

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;

    public PlaylistResponse createPlaylist(Long userId, PlaylistRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Playlist playlist = Playlist.builder()
                .title(request.getTitle())
                .explanation(request.getExplanation())
                .imageUrl(request.getImageUrl())
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

        playlist.setTitle(request.getTitle());
        playlist.setExplanation(request.getExplanation());
        playlist.setImageUrl(request.getImageUrl());

        return playlistRepository.save(playlist);
    }

    public void deletePlaylist(Long playlistId, String username) {
        Playlist playlist = getPlaylistById(playlistId);
        if (!playlist.getUser().getUsername().equals(username)) {
            throw new RuntimeException("본인의 플레이리스트만 삭제할 수 있습니다.");
        }
        playlistRepository.delete(playlist);
    }

    public void removeMusicFromPlaylist(Long playlistId, Long musicId, String username) {
        Playlist playlist = getPlaylistById(playlistId);
        if (!playlist.getUser().getUsername().equals(username)) {
            throw new RuntimeException("본인의 플레이리스트만 수정할 수 있습니다.");
        }

        playlist.getMusics().removeIf(music -> music.getId().equals(musicId));
        playlistRepository.save(playlist);
    }

    public Playlist reorderPlaylist(Long playlistId, List<Long> orderedMusicIds, String username) {
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

    // 🔥 새로운 기능: 플레이리스트에 음악 추가
    public Playlist addMusicToPlaylist(Long playlistId, Long musicId, String username) {
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
