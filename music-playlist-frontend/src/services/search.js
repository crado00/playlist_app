import api from "./api";

const searchService = {
  async searchMusic(text) {
    const response = await api.post("/api/search/integration", { text });
    return response.data;
  },
};

export default searchService;