import { FiLogOut } from "react-icons/fi";
import * as Tabs from "@radix-ui/react-tabs";
import { Home, User } from "lucide-react";
import Profile from "./profile";
import MiniYoutubePlayer from "../components/player/player";
import { useState } from "react";
import MusicSearch from "./MusicSearch";
import useAuthStore from "../store/authStore";
const HomeP = () => {
  const [isPlayer, setIsPlayer] = useState(false);
  const { logout } = useAuthStore();
  
  const hendlerlogout = () => {
    // 로그아웃 로직 구현
    logout();
  };
  return (
    <div className="min-h-screen max-w-2xl mx-auto flex flex-col items-center">
      <header className="fixed top-0 w-full z-40 max-w-2xl bg-white flex justify-between items-center border-b border-gray-400 p-4">
        <h1 className="text-3xl text-black p-2">Music Playlist App</h1>
        <div className="flex justify-end pr-4">
          <button
            className="text-black hover:text-red-500 transition-colors"
            title="Logout"
            aria-label="Logout"
            onClick={hendlerlogout}
          >
            <FiLogOut size={24} />
          </button>
        </div>
      </header>

      <div className="flex-1 w-full max-w-2xl pb-16 pt-15">
        <Tabs.Root defaultValue="home" className="w-full">
          <Tabs.Content value="home" className="text-center">
            <MusicSearch />
          </Tabs.Content>

          <Tabs.Content value="user">
            {/* Profile에 onSelectPlayList 콜백 전달 */}
            <Profile />
          </Tabs.Content>

          {/* 하단 탭바 */}
          <div className="fixed bottom-0 left-1/2 -translate-x-1/2 w-full max-w-2xl z-40 bg-white border-t border-gray-400">
            <Tabs.List className="flex justify-around py-2">
              <Tabs.Trigger
                value="home"
                className="flex flex-col items-center gap-1 text-gray-500 hover:text-black 
                  data-[state=active]:text-black relative pb-2 px-32 py-2
                  after:content-[''] after:absolute after:bottom-0 after:h-[2px] after:w-5 after:bg-black 
                  after:scale-x-0 data-[state=active]:after:scale-x-100 
                  after:transition-transform after:origin-center"
              >
                <Home size={28} />
                <span className="text-xs">홈</span>
              </Tabs.Trigger>
              <Tabs.Trigger
                value="user"
                className="flex flex-col items-center gap-1 text-gray-500 hover:text-black 
                  data-[state=active]:text-black relative pb-2 px-32 py-2
                  after:content-[''] after:absolute after:bottom-0 after:h-[2px] after:w-5 after:bg-black 
                  after:scale-x-0 data-[state=active]:after:scale-x-100 
                  after:transition-transform after:origin-center"
              >
                <User size={28} />
                <span className="text-xs">사용자</span>
              </Tabs.Trigger>
            </Tabs.List>
          </div>
        </Tabs.Root>
      </div>
      {isPlayer && (<MiniYoutubePlayer />)}
    </div>
  );
};

export default HomeP;
