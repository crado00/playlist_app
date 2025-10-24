// MiniYoutubePlayerWithPlaylist.jsx
import React, { useEffect, useRef, useState } from "react";

const playlist = [
  { id: "dQw4w9WgXcQ", title: "Never Gonna Give You Up" },
  { id: "3JZ_D3ELwOQ", title: "Uptown Funk" },
  { id: "L_jWHffIx5E", title: "Smells Like Teen Spirit" },
  { id: "fJ9rUzIMcZQ", title: "Queen - Bohemian Rhapsody" },
  { id: "eY52Zsg-KVI", title: "Imagine Dragons - Believer" },
];


const MiniYoutubePlayerWithPlaylist = () => {
  const playerRef = useRef(null);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [isApiReady, setIsApiReady] = useState(false);

  // 1. IFrame API를 한 번만 로드
  useEffect(() => {
    if (!window.YT) {
      const tag = document.createElement("script");
      tag.src = "https://www.youtube.com/iframe_api";
      document.body.appendChild(tag);

      window.onYouTubeIframeAPIReady = () => {
        setIsApiReady(true);
      };
    } else {
      setIsApiReady(true);
    }
  }, []);

  // 2. API 준비되면 플레이어 생성
  useEffect(() => {
    if (isApiReady && playerRef.current === null) {
      playerRef.current = new window.YT.Player("mini-player", {
        height: "180",
        width: "320",
        videoId: playlist[currentIndex].id,
        events: {
          onStateChange: (event) => {
            if (event.data === window.YT.PlayerState.ENDED) handleNext();
          },
        },
      });
    }
  }, [isApiReady, currentIndex]);

  // 버튼 핸들러
  const handlePlay = () => playerRef.current?.playVideo();
  const handlePause = () => playerRef.current?.pauseVideo();
  const handleNext = () => {
    const nextIndex = (currentIndex + 1) % playlist.length;
    setCurrentIndex(nextIndex);
    playerRef.current?.loadVideoById(playlist[nextIndex].id);
  };
  const handlePrev = () => {
    const prevIndex = (currentIndex - 1 + playlist.length) % playlist.length;
    setCurrentIndex(prevIndex);
    playerRef.current?.loadVideoById(playlist[prevIndex].id);
  };
  const handleSelect = (index) => {
    setCurrentIndex(index);
    playerRef.current?.loadVideoById(playlist[index].id);
  };

  return (
    <div style={{
      position: "fixed",
      bottom: "20px",
      right: "20px",
      width: "360px",
      backgroundColor: "#fff",
      border: "1px solid #ccc",
      borderRadius: "8px",
      boxShadow: "0 2px 8px rgba(0,0,0,0.2)",
      padding: "8px",
      zIndex: 1000,
    }}>
      <div id="mini-player" style={{ width: "320px", height: "180px", marginBottom: "8px" }}></div>
      <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "8px" }}>
        <button onClick={handlePrev}>◀ 이전</button>
        <button onClick={handlePlay}>▶ 재생</button>
        <button onClick={handlePause}>⏸ 일시정지</button>
        <button onClick={handleNext}>다음 ▶</button>
      </div>

      {/* 가로 스크롤 플레이리스트 */}
      <div style={{
        display: "flex",
        overflowX: "auto",
        gap: "8px",
        paddingBottom: "4px"
      }}>
        {playlist.map((item, index) => (
          <div
            key={item.id}
            onClick={() => handleSelect(index)}
            style={{
              minWidth: "100px",
              cursor: "pointer",
              padding: "4px",
              border: index === currentIndex ? "2px solid #007bff" : "1px solid #ccc",
              borderRadius: "4px",
              textAlign: "center",
              backgroundColor: "#f9f9f9",
            }}
          >
            <div style={{ fontSize: "12px", marginBottom: "4px" }}>{item.title}</div>
            <img
              src={`https://img.youtube.com/vi/${item.id}/default.jpg`}
              alt={item.title}
              style={{ width: "100%" }}
            />
          </div>
        ))}
      </div>
    </div>
  );
};

export default MiniYoutubePlayerWithPlaylist;
