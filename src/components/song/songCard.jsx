const SongCard = ({ song, onClick, isAdd }) => {
    
  return (
    <div className="flex justify-between bg-white p-3 rounded-xl shadow hover:shadow-lg transition-shadow">
      <div className="flex flex-col">
        <p className="font-semibold">{song.name}</p>
        <p className="text-sm text-gray-600">{song.singer}</p>
        </div>
        <button
        className="bg-blue-500 text-white px-3 py-1 rounded-lg hover:bg-blue-600 transition-colors"
        onClick={onClick}
        >
        { isAdd ? "추가" : "제거" }
        </button>
    </div>
  );
}

export default SongCard;