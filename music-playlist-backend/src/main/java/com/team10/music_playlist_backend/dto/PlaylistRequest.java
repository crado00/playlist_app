package com.team10.music_playlist_backend.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaylistRequest {
    private String title;
    private String explanation;
    private String imageUrl;
    private List<MusicRequest> musics;
}
