import { create } from "zustand";
import playlistService from "../services/playlist";

const usePlayListStore = create((set) => ({
  playlists: [],
  setPlaylists: (playlists) => set({ playlists }),

  // 1️⃣ 플레이리스트 조회
  fetchPlaylists: async (playlistId) => {
    try {
      const playlist = await playlistService.getPlaylists(playlistId);
      set({ playlists: [playlist] }); // 단일 조회 기준
    } catch (error) {
      console.error("Failed to fetch playlists:", error);
    }
  },

  // 2️⃣ 플레이리스트 생성
  createPlaylist: async (playlistData, username) => {
    try {
      const newPlaylist = await playlistService.createPlaylist(playlistData, username);
      set((state) => ({ playlists: [...state.playlists, newPlaylist] }));
      return newPlaylist;
    } catch (error) {
      console.error("Failed to create playlist:", error);
      throw error;
    }
  },

  // 3️⃣ 플레이리스트 삭제
  deletePlaylist: async (playlistId, username) => {
    try {
      await playlistService.deletePlaylist(playlistId, username);
      set((state) => ({
        playlists: state.playlists.filter((pl) => pl.id !== playlistId),
      }));
    } catch (error) {
      console.error("Failed to delete playlist:", error);
      throw error;
    }
  },

  // 4️⃣ 플레이리스트 수정
  updatePlaylist: async (playlistId, playlistData, username) => {
    try {
      const updatedPlaylist = await playlistService.updatePlaylist(
        playlistId,
        playlistData,
        username
      );
      set((state) => ({
        playlists: state.playlists.map((pl) =>
          pl.id === playlistId ? updatedPlaylist : pl
        ),
      }));
      return updatedPlaylist;
    } catch (error) {
      console.error("Failed to update playlist:", error);
      throw error;
    }
  },

  // 5️⃣ 플레이리스트에서 음악 제거
  removeSongFromPlaylist: async (playlistId, songId, username) => {
    try {
      await playlistService.removeSongFromPlaylist(playlistId, songId, username);
      set((state) => ({
        playlists: state.playlists.map((pl) =>
          pl.id === playlistId
            ? {
                ...pl,
                musics: pl.musics.filter((song) => song.id !== songId),
              }
            : pl
        ),
      }));
    } catch (error) {
      console.error("Failed to remove song from playlist:", error);
      throw error;
    }
  },

  // 6️⃣ 음악 순서 변경
  reorderPlaylist: async (playlistId, orderedMusicIds, username) => {
    try {
      const updatedPlaylist = await playlistService.reorderPlaylist(
        playlistId,
        orderedMusicIds,
        username
      );
      set((state) => ({
        playlists: state.playlists.map((pl) =>
          pl.id === playlistId ? updatedPlaylist : pl
        ),
      }));
      return updatedPlaylist;
    } catch (error) {
      console.error("Failed to reorder playlist:", error);
      throw error;
    }
  },
}));

export default usePlayListStore;
