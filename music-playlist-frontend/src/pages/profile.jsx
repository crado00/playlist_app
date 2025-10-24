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
  const { getPlaylistsByUser, deletePlaylist} = usePlayListStore();
  const [showplaylistCreateModal, setShowPlaylistCreateModal] = useState(false);
  const [isPlaylistEdit, setIsPlaylistEdit] = useState(false);
  const [selectedPlayList, setSelectedPlayList] = useState(null);
  const [selectedEditPlayList, setSelectedEditPlayList] = useState(null);
  const user = localStorage.getItem("user")
    ? JSON.parse(localStorage.getItem("user"))
    : null;

  const [playlist, setPlaylist] = useState([]);
  
  useEffect(() => {
    const fetchData = async () => {
      if (user && user.id) {
        await getUserProfile(user.id);
        await getPlaylistsByUser(); // 스토어가 내부적으로 상태를 바꿈
        const playlistsFromStore = usePlayListStore.getState().playlists;
        setPlaylist(playlistsFromStore);
      }
    };
    console.log("useEffect in profile: " + playlist);
    fetchData();
  }, []);

  useEffect(() => {
    const playlistsFromStore = usePlayListStore.getState().playlists;
    setPlaylist(playlistsFromStore);
  }, [usePlayListStore.getState().playlists]);

    const deletePlayList = (id) => {
        deletePlaylist(id);
      const playlistsFromStore = usePlayListStore.getState().playlists;
        setPlaylist(playlistsFromStore);
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
  return <div className="bg-red-100">
    <div className="bg-white min-h-screen max-w-2xl mx-auto flex flex-col">

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
          deletePlayList={deletePlayList}
          />
        </div>
      </div>

      {showEditModal && (
        <EditProfile
        onClose={() => {
            setShowEditModal(false);
            getUserProfile(userId);
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
