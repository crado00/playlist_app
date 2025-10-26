package com.team10.music_playlist_backend.controller;

import com.team10.music_playlist_backend.dto.SongCharacteristicResponse;
import com.team10.music_playlist_backend.dto.SongDetailsResponse;
import com.team10.music_playlist_backend.service.SpotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/songs")
@RequiredArgsConstructor
public class MusicController {

    private final SpotifyService spotifyService;

    @GetMapping("/details/{songId}")
    public ResponseEntity<SongDetailsResponse> getDetails(@PathVariable String songId) {
        return ResponseEntity.ok(spotifyService.getSongDetails(songId));
    }

    @GetMapping("/characteristic/{songId}")
    public ResponseEntity<SongCharacteristicResponse> getCharacteristic(@PathVariable String songId) {
        return ResponseEntity.ok(spotifyService.getSongCharacteristic(songId));
    }
}