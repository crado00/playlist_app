package com.team10.music_playlist_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchItemResponse {
    private String id;
    private String name;
    private String singer;
    private String album;
    private LocalDate releaseDate;
    private String imageUrl;
    private String previewUrl;
    private List<String> genres;
}