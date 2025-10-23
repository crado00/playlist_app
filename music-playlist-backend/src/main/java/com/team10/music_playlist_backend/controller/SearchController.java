package com.team10.music_playlist_backend.controller;

import com.team10.music_playlist_backend.dto.SearchAdvancedRequest;
import com.team10.music_playlist_backend.dto.SearchIntegrationRequest;
import com.team10.music_playlist_backend.dto.SearchItemResponse;
import com.team10.music_playlist_backend.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @PostMapping("/integration")
    public ResponseEntity<List<SearchItemResponse>> integration(@Valid @RequestBody SearchIntegrationRequest req) {
        return ResponseEntity.ok(searchService.integration(req));
    }

    @PostMapping("/advanced")
    public ResponseEntity<List<SearchItemResponse>> advanced(@Valid @RequestBody SearchAdvancedRequest req) {
        return ResponseEntity.ok(searchService.advanced(req));
    }
}