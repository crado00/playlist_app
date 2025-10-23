import React, { useEffect } from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import HomeP from "./pages/home";
import Signup from "./pages/signup";
import Login from "./pages/login";
import Profile from "./pages/profile";
import useAuthStore from "./store/authStore";

const App = () => {
  const { isAuthenticated } = useAuthStore();
  
  useEffect(() =>{
    console.log(isAuthenticated);
  })

  return (
    <BrowserRouter>
      <Routes>
        {/* 로그인 관련 */}
        <Route
          path="/"
          element={isAuthenticated ? <HomeP /> : <Navigate to="/login" />}
        />
        <Route
          path="/login"
          element={isAuthenticated ? <Navigate to="/" /> : <Login />}
        />
        <Route
          path="/signup"
          element={isAuthenticated ? <Navigate to="/" /> : <Signup />}
        />

        {/* 프로필 관련 */}
        <Route path="/profile" element={<Profile />} />
      </Routes>
    </BrowserRouter>
  );
};

export default App;

