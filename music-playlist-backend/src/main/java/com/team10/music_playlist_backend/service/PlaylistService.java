package com.team10.music_playlist_backend.service;

import com.team10.music_playlist_backend.dto.*;
import com.team10.music_playlist_backend.entity.*;
import com.team10.music_playlist_backend.exception.ResourceNotFoundException;
import com.team10.music_playlist_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final UserRepository userRepository;
    private final MusicRepository musicRepository;

    @Transactional
    public PlaylistResponse createPlaylist(Long userId, PlaylistRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        Playlist playlist = Playlist.builder()
                .title(request.getTitle())
                .explanation(request.getExplanation())
                .imageUrl(request.getImageUrl())
                .user(user)
                .build();

        if (request.getMusics() != null) {
            request.getMusics().forEach(musicDto -> {
                Music music = Music.builder()
                        .title(musicDto.getTitle())
                        .artist(musicDto.getArtist())
                        .album(musicDto.getAlbum())
                        .genre(musicDto.getGenre())
                        .duration(musicDto.getDuration())
                        .youtubeUrl(musicDto.getYoutubeUrl())
                        .playlist(playlist)
                        .build();
                playlist.getMusics().add(music);
            });
        }

        playlistRepository.save(playlist);
        return mapToResponse(playlist);
    }

    public List<PlaylistResponse> getUserPlaylists(Long userId) {
        List<Playlist> playlists = playlistRepository.findByUserId(userId);
        return playlists.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public PlaylistResponse getPlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("플레이리스트를 찾을 수 없습니다."));
        return mapToResponse(playlist);
    }

    @Transactional
    public void deletePlaylist(Long playlistId, String userId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("플레이리스트를 찾을 수 없습니다."));
        if (!playlist.getUser().getId().equals(userId)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
        playlistRepository.delete(playlist);
    }

    @Transactional
    public Playlist editPlaylist(Long playlistId, PlaylistEditRequest request, String userId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("플레이리스트를 찾을 수 없습니다."));

        if (!playlist.getUser().getId().equals(userId)) {
            throw new RuntimeException("이 플레이리스트를 수정할 권한이 없습니다.");
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            playlist.setTitle(request.getTitle());
        }

        if (request.getExplanation() != null) {
            playlist.setExplanation(request.getExplanation());
        }

        if (request.getMusicIds() != null && !request.getMusicIds().isEmpty()) {
            // 기존 Music 제거
            playlist.getMusics().clear();

            List<Music> musics = musicRepository.findAllById(request.getMusicIds());
            musics.forEach(music -> music.setPlaylist(playlist));
            playlist.getMusics().addAll(musics);
        }

        return playlistRepository.save(playlist);
    }

    public Playlist getPlaylistById(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
    }

    @Transactional
    public void removeMusicFromPlaylist(Long playlistId, Long musicId, String username) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("플레이리스트를 찾을 수 없습니다."));

        if (!playlist.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 플레이리스트를 수정할 권한이 없습니다.");
        }

        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new ResourceNotFoundException("음악을 찾을 수 없습니다."));

        playlist.getMusics().removeIf(m -> m.getId().equals(music.getId()));
        playlistRepository.save(playlist);
    }

    private PlaylistResponse mapToResponse(Playlist playlist) {
        List<MusicResponse> musicResponses = playlist.getMusics().stream()
                .map(MusicResponse::fromEntity)
                .collect(Collectors.toList());

        return PlaylistResponse.builder()
                .id(playlist.getId())
                .title(playlist.getTitle())
                .explanation(playlist.getExplanation())
                .imageUrl(playlist.getImageUrl())
                .musics(musicResponses)
                .build();
    }
}
