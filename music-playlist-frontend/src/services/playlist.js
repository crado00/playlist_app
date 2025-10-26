import api from "./api";

const playListService = {
  // 1️⃣ 플레이리스트 조회
  async getPlaylists(playListId) {
    const response = await api.get(`/api/playlists/${playListId}`);
    return response.data;
  },

  // 2️⃣ 플레이리스트 생성
  async createPlaylist(playlistData, username) {
    const response = await api.post("/api/playlists/create", playlistData, {
      params: { username },
    });
    return response.data;
  },

  // 3️⃣ 플레이리스트 삭제
  async deletePlaylist(playlistId, username) {
    const response = await api.delete(`/api/playlists/${playlistId}`, {
      params: { username },
    });
    return response.data;
  },

  // 4️⃣ 플레이리스트 수정
  async updatePlaylist(playlistId, playlistData, username) {
    const response = await api.put(`/api/playlists/${playlistId}/edit`, playlistData, {
      params: { username },
    });
    console.log("Updated playlist response:", response.data);
    return response.data;
  },

  // 5️⃣ 플레이리스트에서 음악 제거
  async removeSongFromPlaylist(playlistId, songId, username) {
    const response = await api.delete(`/api/playlists/${playlistId}/musics/${songId}`, {
      params: { username },
    });
    return response.data;
  },

  // 6️⃣ 음악 순서 변경
  async reorderPlaylist(playlistId, orderedMusicIds, username) {
    const response = await api.put(`/api/playlists/${playlistId}/reorder`, orderedMusicIds, {
      params: { username },
    });
    return response.data;
  },

  async getPlaylistsByUser(username) {
  console.log("Fetching playlists for user:", username);
  const response = await api.get(`/api/playlists/my`, {
    params: { username }, // ✅ 이게 핵심
  });
  console.log("Fetched user's playlists:", response.data);
  return response.data;
}

};

export default playListService;
