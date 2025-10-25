package com.team10.music_playlist_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchAdvancedRequest {
    @NotBlank
    private String text;
    @NotBlank
    private String genre;
}