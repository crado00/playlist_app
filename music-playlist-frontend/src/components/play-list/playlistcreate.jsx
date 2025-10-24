import { useEffect, useState } from "react";
import { FiX } from "react-icons/fi";
import { authService } from "../../services/auth";
import usePlayListStore from "../../store/playlistStore";
import useSearchStore from "../../store/searchStore";
import Slider from "react-slick";
import SongCard from "../song/songCard";

const PlayListCreate = ({ onClose, onCreate, isEdit, playlist }) => {
  const [user, setUser] = useState(null);
  const { createPlaylist, updatePlaylist } = usePlayListStore();
  const { searchMusic } = useSearchStore();
  const [searchQuery, setSearchQuery] = useState("");
  const [searchResults, setSearchResults] = useState([]);
  const [ addSong, setAddSong] = useState([]);

  
  useEffect(() => {
    const currentUser = authService.getCurrentUser();
    setUser(currentUser);
  }, []);
  
  useEffect(() => {
    if (isEdit && playlist && playlist.musics) {
      setAddSong(playlist.musics);
    }
  }, [isEdit, playlist]);
  
  useEffect(() => {
    if (searchQuery.trim() === "") return;
    searchMusic(searchQuery).then((results) => {
      setSearchResults(results);
      console.log("Search results:", results);
    }).catch((error) => {
      console.error("Search error:", error);
    });
  }, [searchQuery]);

  const handleCreateOrEdit = () => {
    const name = document.getElementById("playlist-name").value;
    const explanation = document.getElementById("playlist-description").value;
    if (isEdit) {
      const playlistId = playlist.id;
      const playlistData = { title: name, explanation, userId: user.id, musics: addSong };
      updatePlaylist(playlistId, playlistData);
    } else {
      const playlist = {title: name, explanation, userId: user.id, musics: addSong };
      //이름 변경
      createPlaylist(playlist);
    }
    onCreate();
  };

  const handleAddSong = (song) => {
    // 노래 추가 로직 구현
    setAddSong([...addSong, song]);
  }

  const handleRemoveSong = (song) => {
    setAddSong(addSong.filter(s => s.id !== song.id));
  }
  
  return (
      <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4">
        <div className="bg-white rounded-lg w-full max-w-4xl max-h-[800px] h-full flex flex-col">
          <div className="flex items-center justify-between p-4 border-b border-gray-200">
            <h2 className="text-xl font-bold">플레이 리스트 {isEdit ? "수정" : "생성"}</h2>
            <button
              onClick={onClose}
              className="p-2 hover:bg-gray-100 rounded-full transition-colors"
            >
            <FiX size={24} />
          </button>
        </div>
          <div className="flex flex-row  h-full">
          <div className="w-1/2 border-r">
          <div className="p-4">
            <label className="block mb-2 font-semibold text-gray-700" htmlFor="playlist-name">
              플레이 리스트 이름
            </label>
            <input
              id="playlist-name"
              name="playlist-name"
              className="w-full border p-2 rounded-lg focus:ring-2 focus:ring-blue-400"
              placeholder="플레이 리스트 이름을 입력하세요"
              defaultValue={isEdit && playlist ? playlist.title : ""}
            />
          </div>
          <div className="p-4">
            <label className="block mb-2 font-semibold text-gray-700" htmlFor="playlist-description">
              설명
            </label>
            <textarea
              id="playlist-description"
              name="playlist-description"
              className="w-full border p-2 rounded-lg focus:ring-2 focus:ring-blue-400"
              placeholder="플레이 리스트 설명을 입력하세요"
              rows={3}
              defaultValue={isEdit && playlist ? playlist.explanation : ""}
            />
          </div>
          <div className="p-4 w-full border-b border-gray-200">
            {/* 추가된 음악 */}
            <h3 className="font-semibold mb-2">추가된 음악</h3>
            <div className="overflow-y-auto border bg-gray-100 p-4 rounded-2xl h-73">
              {/* 추가된 음악 목록 */}
              <Slider
                dots={false}
                infinite={false}
                speed={500}
                slidesToShow={1}
                slidesToScroll={1}
                vertical={true}
                verticalSwiping={true}
                arrows={false}
                className="h-full rounded-2xl overflow-y-auto"
              >
                {/* 여기에 추가된 음악들을 매핑 */}
                {addSong.map((song) => (<div key={song.id} className="flex justify-between items-center p-2 border-b hover:bg-gray-200 cursor-pointer">
                  <SongCard song={song} onClick={() => handleRemoveSong(song)} isAdd={false} />

                </div>))}

              </Slider>
            </div>
          </div>
          </div>
          <div className="w-1/2 border-l p-4">
            {/* 음악 검색 및 추가 */}
            <h3 className="font-semibold mb-2">음악 검색</h3>
            <input
              type="text"
              className="w-full border p-2 rounded-t-2xl focus:ring-2 focus:ring-blue-400"
              placeholder="음악을 검색하세요"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
            <div className="h-130 border bg-gray-100 p-4 rounded-b-2xl">
              <Slider
                dots={false}
                infinite={false}
                speed={500}
                slidesToShow={1}
                slidesToScroll={1}
                vertical={true}
                verticalSwiping={true}
                arrows={false}
                className="h-full overflow-y-auto"
              >
                {searchResults.map((song) => (
                  <div
                    key={song.id}
                    className="flex justify-between items-center p-2 border-b hover:bg-gray-200 cursor-pointer"
                  >
                    <SongCard song={song} onClick={() => handleAddSong(song)} isAdd={true}/>
                  </div>

                ))}

              </Slider>
            </div>
          </div>
          </div>
          <div className="flex justify-end p-4 border-t border-gray-200">
            <button
              onClick={onClose}
              className="bg-gray-300 text-gray-700 px-4 py-2 rounded-lg mr-2 hover:bg-gray-400 transition-colors"
            >
              취소
            </button>
            <button
              onClick={handleCreateOrEdit}
              className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors"
            >
              {isEdit ? "수정" : "생성"}
            </button>
          </div>
        </div>
    </div>
  );
};

export default PlayListCreate;