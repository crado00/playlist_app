import api from "./api";

const userService = {
  getUserProfile: async (userId) => {
    const response = await api.get(`/api/users/${userId}`);
    return response.data;
  },

  // ✅ 오타 수정 (updateProfile)
  updateProfile: async (data, userId) => {
    const response = await api.put(`/api/users/${userId}`, data);
    return response.data;
  },
};

export default userService;
