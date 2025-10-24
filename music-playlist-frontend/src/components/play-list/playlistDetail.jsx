import Button from "../common/Button";

const PlayListDetail = ({ playlist, onClose, onEdit }) => {
  const songs = [
    { id: 1, title: "Song A" },
    { id: 2, title: "Song B" },
    { id: 3, title: "Song C" },
  ];

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
      <div className="bg-white w-11/12 max-w-lg rounded-xl p-6 relative">
        <button
          className="absolute top-3 right-3 text-gray-500 hover:text-black font-bold text-lg"
          onClick={onClose}
        >
          ✕
        </button>

        <h2 className="text-2xl font-bold mb-2">{playlist.name}</h2>
        <p className="text-gray-600 mb-4">{playlist.explanation}</p>

        <div className="flex flex-col gap-3">
          {songs.map((song) => (
            <div
              key={song.id}
              className="flex justify-between items-center p-2 border rounded hover:bg-gray-100"
            >
              <span>{song.title}</span>
              <Button
                title="재생"
                customStyles="bg-blue-500 hover:bg-blue-600 text-white px-3 py-1 rounded"
                onClick={() => console.log(`재생 버튼 클릭: ${song.title}`)}
              />
            </div>
          ))}

        {onEdit && (
          <Button
            title="변경"
            customStyles="bg-green-500 hover:bg-green-600 mb-4"
            onClick={() => onEdit && onEdit(playlist)}
          />
        )}
        </div>
      </div>
    </div>
  );
};

export default PlayListDetail;
