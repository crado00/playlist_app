package com.team10.music_playlist_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongCharacteristicResponse {
    private Integer bpm;
    private String key;
}