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

import java.util.*;
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
                Music music = new Music();
//                Music music = Music.builder()
//                        .title(musicDto.getTitle())
//                        .artist(musicDto.getArtist())
//                        .album(musicDto.getAlbum())
//                        .genre(musicDto.getGenre())
//                        .duration(musicDto.getDuration())
//                        .youtubeUrl(musicDto.getYoutubeUrl())
//                        .playlist(playlist)
//                        .build();
                playlist.getMusics().add(music);
            });
        }

        playlistRepository.save(playlist);
        return PlaylistResponse.fromEntity(playlist);
    }

    public Playlist getPlaylistById(Long playlistId) {
        return playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("플레이리스트를 찾을 수 없습니다."));
    }

    @Transactional
    public Playlist editPlaylist(Long playlistId, PlaylistEditRequest request, String username) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("플레이리스트를 찾을 수 없습니다."));

        if (!playlist.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            playlist.setTitle(request.getTitle());
        }
        if (request.getExplanation() != null) {
            playlist.setExplanation(request.getExplanation());
        }

        if (request.getMusicIds() != null && !request.getMusicIds().isEmpty()) {
            //List<Music> musics = musicRepository.findAllById(request.getMusicIds());
            //musics.forEach(m -> m.setPlaylist(playlist));
            List<Music> musics = new ArrayList<>();
            playlist.getMusics().clear();
            playlist.getMusics().addAll(musics);
        }

        return playlistRepository.save(playlist);
    }

    @Transactional
    public void deletePlaylist(Long playlistId, String username) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("플레이리스트를 찾을 수 없습니다."));

        if (!playlist.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
        }

        playlistRepository.delete(playlist);
    }

    @Transactional
    public void removeMusicFromPlaylist(Long playlistId, Long musicId, String username) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("플레이리스트를 찾을 수 없습니다."));

        if (!playlist.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

        //playlist.getMusics().removeIf(m -> m.getId().equals(musicId));
        playlistRepository.save(playlist);
    }

    @Transactional
    public Playlist reorderPlaylist(Long playlistId, List<Long> orderedMusicIds, String username) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("플레이리스트를 찾을 수 없습니다."));

        if (!playlist.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }

//        Map<Long, Music> musicMap = playlist.getMusics().stream()
//                .collect(Collectors.toMap(Music::getId, m -> m));
        Map<Long, Music> musicMap = new HashMap<>();
        List<Music> newOrder = orderedMusicIds.stream()
                .map(musicMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        playlist.getMusics().clear();
        playlist.getMusics().addAll(newOrder);

        return playlistRepository.save(playlist);
    }
}
