package com.team10.music_playlist_backend.repository;

import com.team10.music_playlist_backend.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // 생략 가능하지만 명시하면 안전
public interface MusicRepository extends JpaRepository<Music, String> {
}
