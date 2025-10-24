package com.team10.music_playlist_backend.controller;

import com.team10.music_playlist_backend.dto.PlaylistEditRequest;
import com.team10.music_playlist_backend.dto.PlaylistRequest;
import com.team10.music_playlist_backend.dto.PlaylistResponse;
import com.team10.music_playlist_backend.entity.Playlist;
import com.team10.music_playlist_backend.repository.UserRepository;
import com.team10.music_playlist_backend.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;
    private final UserRepository userRepository;

    // 1️⃣ 플레이리스트 생성
    @PostMapping("/create")
    public ResponseEntity<PlaylistResponse> createPlaylist(
            @RequestBody PlaylistRequest request,
            @RequestParam String username) {

        Long userId = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."))
                .getId();

        PlaylistResponse playlistResponse = playlistService.createPlaylist(userId, request);
        return ResponseEntity.ok(playlistResponse);
    }

    // 2️⃣ 특정 플레이리스트 조회
    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponse> getPlaylistById(@PathVariable Long playlistId) {
        Playlist playlist = playlistService.getPlaylistById(playlistId);
        return ResponseEntity.ok(PlaylistResponse.fromEntity(playlist));
    }

    // 3️⃣ 로그인한 유저의 플레이리스트 목록 조회
    @GetMapping("/my")
    public ResponseEntity<List<PlaylistResponse>> getMyPlaylists(@RequestParam String username) {
        List<PlaylistResponse> playlists = playlistService.getUserPlaylists(username);
        return ResponseEntity.ok(playlists);
    }

    // 4️⃣ 플레이리스트 수정
    @PutMapping("/{playlistId}/edit")
    public ResponseEntity<PlaylistResponse> editPlaylist(
            @PathVariable Long playlistId,
            @RequestBody PlaylistEditRequest request,
            @RequestParam String username) {

        Playlist playlist = playlistService.editPlaylist(playlistId, request, username);
        return ResponseEntity.ok(PlaylistResponse.fromEntity(playlist));
    }

    // 5️⃣ 플레이리스트 삭제
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(
            @PathVariable Long playlistId,
            @RequestParam String username) {

        playlistService.deletePlaylist(playlistId, username);
        return ResponseEntity.noContent().build();
    }

    // 6️⃣ 플레이리스트에서 음악 제거
    @DeleteMapping("/{playlistId}/musics/{musicId}")
    public ResponseEntity<Void> removeMusicFromPlayList(
            @PathVariable Long playlistId,
            @PathVariable Long musicId,
            @RequestParam String username) {

        playlistService.removeMusicFromPlaylist(playlistId, musicId, username);
        return ResponseEntity.noContent().build();
    }

    // 7️⃣ 음악 순서 변경
    @PutMapping("/{playlistId}/reorder")
    public ResponseEntity<PlaylistResponse> reorderPlaylist(
            @PathVariable Long playlistId,
            @RequestBody List<Long> orderedMusicIds,
            @RequestParam String username) {

        Playlist playlist = playlistService.reorderPlaylist(playlistId, orderedMusicIds, username);
        return ResponseEntity.ok(PlaylistResponse.fromEntity(playlist));
    }

    // 8️⃣ 플레이리스트에 음악 추가
    @PostMapping("/{playlistId}/musics")
    public ResponseEntity<PlaylistResponse> addMusicToPlaylist(
            @PathVariable Long playlistId,
            @RequestParam Long musicId,
            @RequestParam String username) {

        Playlist playlist = playlistService.addMusicToPlaylist(playlistId, musicId, username);
        return ResponseEntity.ok(PlaylistResponse.fromEntity(playlist));
    }
}
