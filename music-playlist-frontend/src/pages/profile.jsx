import { use, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import ProfileInfo from "../components/profile/ProfileInfo";
import ProfilePlayList from "../components/play-list/ProfilePlayList";
import useUserStore from "../store/userStore";
import EditProfile from "../components/profile/editProfile";
import usePlayListStore from "../store/playlistStore";
import PlayListCreate from "../components/play-list/playlistcreate";
import PlayListDetail from "../components/play-list/playlistDetail";

const Profile = () => {
  const { userProfile, getUserProfile, updateProfile } = useUserStore(); // ✅ 이름 수정
  const [showEditModal, setShowEditModal] = useState(false);
  const { createPlayList } = usePlayListStore();
  const [showplaylistCreateModal, setShowPlaylistCreateModal] = useState(false);
  const [isPlaylistEdit, setIsPlaylistEdit] = useState(false);
  const [selectedPlayList, setSelectedPlayList] = useState(null);
  const [selectedEditPlayList, setSelectedEditPlayList] = useState(null);
  const user = localStorage.getItem("user")
    ? JSON.parse(localStorage.getItem("user"))
    : null;

  // 임시 플레이리스트 데이터
  const playlist = [
    { id: 1, name: "playlist 1", explanation: "hello" },
    { id: 2, name: "User 2", explanation: "hi" },
    { id: 3, name: "User 3", explanation: "good" },
    { id: 4, name: "User 4", explanation: "day" },
    { id: 5, name: "User 5", explanation: "nice" },
  ];



  useEffect(() => {
    if (user && user.id) {
      getUserProfile(user.id);
    }
  }, []);



    const deletePlayList = (id) => {
        console.log("delete playlist", id);
    };

    const playlistCreateOrEdit = () => {
        setShowPlaylistCreateModal(false);
    };

    const playlistCreate = () => {
        setIsPlaylistEdit(false);
        setShowPlaylistCreateModal(true);
    };
    const onSelectPlayList = (playlist) => {
      setSelectedPlayList(playlist);
    };
    const handleEditClick = (playlist) => {
      setIsPlaylistEdit(true);
      setSelectedEditPlayList(selectedPlayList);
      setSelectedPlayList(null);
      setShowPlaylistCreateModal(true);
    }
  const playListSize = playlist ? playlist.length : 0;

  return (
    <div className="bg-red-100">
      <div className="bg-white min-h-screen max-w-2xl mx-auto">
        <ProfileInfo
          user={user}
          playListSize={playListSize}
          onEditProfile={() => setShowEditModal(true)}
        />

        <div className="flex-grow">
          <ProfilePlayList
          playList={playlist}
          createPlayList={playlistCreate}
          onSelect={onSelectPlayList}
          />
        </div>
      </div>

      {showEditModal && (
        <EditProfile
          user={user}
          onClose={() => {
            setShowEditModal(false);
            getUserProfile(user.id);
          }}
          currentProfile={userProfile} // ✅ 함수가 아니라 실제 데이터 전달
        />
      )}
      {showplaylistCreateModal && (
        <PlayListCreate
          onClose={() => setShowPlaylistCreateModal(false)}
          onCreate={playlistCreateOrEdit}
          isEdit={isPlaylistEdit}
          playlist={isPlaylistEdit ? selectedEditPlayList : null}
        />
      )}
      {selectedPlayList && (
        <PlayListDetail
          playlist={selectedPlayList}
          onClose={() => setSelectedPlayList(null)}
          onEdit={handleEditClick}
      />
)}
    </div>
  );
};

export default Profile;
