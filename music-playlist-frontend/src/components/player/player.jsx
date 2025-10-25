import { useEffect, useState, useRef } from "react";
import { FiX } from "react-icons/fi";
import Slider from "react-slick";

const MiniItunesPlayer = ({ playlist, setPlayer }) => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [track, setTrack] = useState(null);
  const [loading, setLoading] = useState(true);
  const audioRef = useRef(null);

  // iTunes API에서 현재 곡 가져오기
  const fetchTrack = async (song) => {
    if (!song) return;
    try {
      const query = encodeURIComponent(`${song.name} ${song.singer}`);
      const response = await fetch(`https://itunes.apple.com/search?term=${query}&entity=musicTrack`);
      const data = await response.json();
      if (data.results && data.results.length > 0) {
        setTrack(data.results[0]);
      } else {
        setTrack(null);
      }
    } catch (error) {
      console.error("iTunes API 오류:", error);
      setTrack(null);
    } finally {
      setLoading(false);
    }
  };

  // playlist나 currentIndex가 바뀌면 해당 곡 가져오기
  useEffect(() => {
    console.log("MiniItunesPlayer playlist or index changed:", playlist, currentIndex);
    if (!playlist || playlist.length === 0) {
      setLoading(false);
      return;
    }
    setLoading(true);
    fetchTrack(playlist[currentIndex]);
  }, [playlist, currentIndex]);

  // 다음 곡 자동 재생
  const handleEnded = () => {
    const nextIndex = (currentIndex + 1) % playlist.length;
    setCurrentIndex(nextIndex);
  };

  if (loading) return <div>불러오는 중...</div>;
  if (!track) return <div>음악 정보를 불러올 수 없습니다.</div>;

  return (
    <div className="p-4 flex flex-col items-center bg-gray-100 rounded-xl shadow-xl shadow-black/30 fixed bottom-4 right-4 z-50 border w-sm h-sm">
      <div className="flex flex-col items-end m-2 w-full">
        <FiX size={20} className="cursor-pointer" onClick={() => setPlayer([])} />
      </div>
      <img
        src={track.artworkUrl100}
        alt={track.trackName}
        className="rounded-xl w-32 h-32 mb-3"
      />
      <h2 className="text-lg font-semibold">{track.trackName}</h2>
      <p className="text-gray-600 mb-3">{track.artistName}</p>

      <audio
        ref={audioRef}
        controls
        src={track.previewUrl}
        className="w-full mt-2 rounded"
        onEnded={handleEnded}
      >
        <source src={track.previewUrl} type="audio/mpeg" />
        브라우저가 오디오 태그를 지원하지 않습니다.
      </audio>

      {/* 전체 리스트 보여주기 */}
      <div className="flex gap-2 overflow-x-auto mt-4">
          {playlist.map((song, index) => (
            <div
              key={song.id}
              onClick={() => setCurrentIndex(index)}
              className={`p-2 cursor-pointer border rounded ${
                index === currentIndex ? "border-blue-500" : "border-gray-300"
              }`}
            >
              <p className="text-sm">{song.name}</p>
              <p className="text-xs text-gray-600">{song.singer}</p>
            </div>
          ))}
      </div>
    </div>
  );
};

export default MiniItunesPlayer;
