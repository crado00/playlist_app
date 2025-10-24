package com.team10.music_playlist_backend.dto;

import com.team10.music_playlist_backend.entity.Music;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongDetailsResponse {
    private Long id;
    private String name;
    private String singer;
    private String explanation;
    private LocalDate releaseDate;

    public static SongDetailsResponse fromEntity(Music music) {
        return SongDetailsResponse.builder()
                .id(music.getId())
                .name(music.getTitle())
                .singer(music.getArtist())
                .build();
    }

}