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

    {/* 음악 정보 연결 */}
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
    <div className="min-h-screen flex flex-col justify-center items-center bg-gradient-to-br from-green-500 to-blue-100">
      <div className="max-w-[800px] space-y-6 my-12">
        <div className="bg-white/95 backdrop-blur-sm rounded-3xl shadow-2xl px-12 py-14">
          <h1 className="text-center mb-6">
            <span className="text-5xl bg-gradient-to-r bg-green-500 bg-clip-text text-transparent">
              🎵 MusicSearch
            </span>
          </h1>
      <form onSubmit={searchMusic} className="flex mb-2">
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

      {loading && <p className="text-center">검색 중...</p>}

      <div className="grid grid-cols-2 sm:grid-cols-3 gap-4">
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
    </div>
    </div>
  );
}

export default MusicSearch;
