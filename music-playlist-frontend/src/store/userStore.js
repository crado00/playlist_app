import userService from "../services/user";
import { create } from "zustand";

const useUserStore = create((set) => ({
  userProfile: null,
  loading: false,
  error: null,

  getUserProfile: async (userId) => {
    set({ loading: true, error: null });
    try {
      const userProfile = await userService.getUserProfile(userId);
      set({ userProfile, loading: false });
      return { userProfile };
    } catch (err) {
      set({
        error: err.response?.data.message || "Failed to get user profile",
        loading: false,
      });
      throw err;
    }
  },

  // ✅ 이름 통일 (updateProfile)
  updateProfile: async (data, userId) => {
    set({ loading: true, error: null });
    try {
      const updatedProfile = await userService.updateProfile(data, userId);
      set({ userProfile: updatedProfile, loading: false });
      return { updatedProfile };
    } catch (err) {
      set({
        error: err.response?.data.message || "Failed to update profile",
        loading: false,
      });
      throw err;
    }
  },
}));

export default useUserStore;
