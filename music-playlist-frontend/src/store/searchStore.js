import searchService from "../services/search";

const useSearchStore = () => {
  const searchMusic = async (query) => {
    try {
      const results = await searchService.searchMusic(query);
      return results;
    } catch (error) {
      console.error("Search error:", error);
      throw error;
    }
  };

  return {
    searchMusic,
  };
};

export default useSearchStore;