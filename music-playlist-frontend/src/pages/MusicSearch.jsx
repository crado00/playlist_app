import React, { useState } from "react";

function MusicSearch() {
  const [query, setQuery] = useState("");
  const [songs, setSongs] = useState([]);
  const [loading, setLoading] = useState(false);

  const searchMusic = async (e) => {
    e.preventDefault();
    if (!query.trim()) return;

    setLoading(true);
    setSongs([]);

    try {
      const response = await fetch(
        `https://itunes.apple.com/search?term=${encodeURIComponent(query)}&media=music&limit=12`
      );
      const data = await response.json();
      setSongs(data.results);
    } catch (err) {
      console.error("검색 오류:", err);
    } finally {
      setLoading(false);
    }
  };

  return (
<div className="min-h-screen flex flex-col items-center ">
  {/* 상단 검색 영역 컨테이너 */}
  <div className="w-full max-w-[800px] bg-white/95 backdrop-blur-sm rounded-3xl shadow-2xl px-8 py-6 flex flex-col items-center space-y-4">
    {/* MusicSearch 제목 */}
    <h1 className="text-3xl font-bold bg-gradient-to-r from-green-500 to-blue-500 bg-clip-text text-transparent">
      🎵 MusicSearch
    </h1>

    {/* 검색창 */}
    <form onSubmit={searchMusic} className="w-full flex mt-2">
      <input
        type="text"
        placeholder="가수 또는 곡 제목 검색"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        className="flex-1 p-2 border rounded-l-lg focus:outline-none"
      />
      <button
        type="submit"
        className="bg-blue-400 text-white px-4 rounded-r-lg hover:bg-blue-600"
      >
        검색
      </button>
    </form>
  </div>

  {/* 검색 결과 */}
  <div className="w-full max-w-[800px] mt-8 grid grid-cols-2 sm:grid-cols-3 gap-4">
    {loading && <p className="text-center col-span-full">검색 중...</p>}
    {songs.map((song) => (
      <div key={song.trackId} className="bg-white p-3 rounded-xl shadow">
        <img
          src={song.artworkUrl100}
          alt={song.trackName}
          className="rounded-lg mb-2 w-full"
        />
        <p className="font-semibold text-sm">{song.trackName}</p>
        <p className="text-gray-600 text-xs">{song.artistName}</p>
        {song.previewUrl && (
          <audio controls className="w-full mt-2">
            <source src={song.previewUrl} type="audio/mpeg" />
          </audio>
        )}
      </div>
    ))}
  </div>
</div>
  );
}

export default MusicSearch;
