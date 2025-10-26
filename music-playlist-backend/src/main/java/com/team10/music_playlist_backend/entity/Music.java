package com.team10.music_playlist_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class Music {

    @Id
    private String id;

    private String title;
    private String artist;
    private String album;
    private String imageUrl;
}