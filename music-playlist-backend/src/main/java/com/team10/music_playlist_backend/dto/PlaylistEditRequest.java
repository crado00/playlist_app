package com.team10.music_playlist_backend.dto;

import com.team10.music_playlist_backend.entity.Music;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistEditRequest {
    private String title;
    private String explanation;
    private List<Music> musics;
}
