import { FiLogOut } from "react-icons/fi";
import * as Tabs from "@radix-ui/react-tabs";
import { Home, User } from "lucide-react";
import Profile from "./profile";
import MiniYoutubePlayer from "../components/player/player";
import { useState } from "react";

const HomeP = () => {
  const [isPlayer, setIsPlayer] = useState(true);
  
  return (
    <div className="bg-red-300 min-h-screen max-w-2xl mx-auto flex flex-col items-center">
        <header className="fixed top-0 w-full z-40 max-w-2xl bg-white flex justify-between items-center border-b border-gray-400 p-4">
            <h1 className="text-3xl text-black p-2">Music Playlist App</h1>
            <div className="flex justify-end pr-4">
                <button
                    className="text-black hover:text-red-500 transition-colors"
                    title="Logout"
                    aria-label="Logout"
                >
                    <FiLogOut size={24} />
                </button>
            </div>
        </header>
      {/* 본문 */}
      <div className="flex-1 w-full max-w-2xl pb-16 pt-20">
        <Tabs.Root defaultValue="home" className="w-full">
          {/* 탭 콘텐츠 */}
          <Tabs.Content value="home" className="p-4 text-center">
            <h2 className="text-lg font-medium mb-2">홈 화면</h2>
            <p className="text-gray-600">여기서 최신 플레이리스트를 확인할 수 있습니다.</p>
          </Tabs.Content>

          <Tabs.Content value="user">
            <Profile />
          </Tabs.Content>

          {/* 하단 탭바 */}
          <div className="fixed bottom-0 left-1/2 -translate-x-1/2 w-full max-w-2xl z-40 bg-white border-t border-gray-400">
            <Tabs.List className="flex justify-around py-2">
              <Tabs.Trigger
                value="home"
                  className="flex flex-col items-center gap-1 text-gray-500 hover:text-black 
                  data-[state=active]:text-black relative pb-1
                    after:content-[''] after:absolute after:bottom-0 after:h-[2px] after:w-5 after:bg-black 
                    after:scale-x-0 data-[state=active]:after:scale-x-100 
                    after:transition-transform after:origin-center"
                >
                <Home size={24} />
                <span className="text-xs">홈</span>
              </Tabs.Trigger>
                <div className="border "></div>
              <Tabs.Trigger
                value="user"
                className="flex flex-col items-center gap-1 text-gray-500 hover:text-black 
                  data-[state=active]:text-black relative pb-1
                  after:content-[''] after:absolute after:bottom-0 after:h-[2px] after:w-5 after:bg-black 
                  after:scale-x-0 data-[state=active]:after:scale-x-100 
                  after:transition-transform after:origin-center"
              >
                <User size={24} />
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