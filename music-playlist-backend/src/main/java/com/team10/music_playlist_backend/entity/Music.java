package com.team10.music_playlist_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "musics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    // Playlist와의 ManyToOne 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id") // FK 컬럼
    private Playlist playlist;
}
