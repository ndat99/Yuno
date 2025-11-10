// File: src/App.tsx
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { HomePage } from "./pages/HomePage";
import { LoginPage } from "./pages/LoginPage";
import { RegisterPage } from "./pages/RegisterPage";
import { Navbar } from "./components/Navbar";
import { useState } from "react";

interface AuthUser{
  id: number;
  name: string;
  username: string;
}

function App() {
  //lấy token từ localStorage để làm giá trị ban đầu
  const [token, setToken] = useState(localStorage.getItem("token"));
  //lấy user từ localStorage
  const [authUser, setAuthUser] = useState<AuthUser | null>(() => {
    const userJson = localStorage.getItem("user");
    if (userJson){
      return JSON.parse(userJson) as AuthUser; //Parse chuỗi string về đối tượng (object)
    }
    return null; //chưa đăng nhập
  });

  //hàm đăng xuất trung tâm
  //dời logic logout từ navBar sang đây
  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    setToken(null);
    setAuthUser(null);
  }

  //hàm đăng nhập trung tâm
  //thay vì truyền setToken, ta truyền hàm này
  const handleLogin = (token: string, user: AuthUser) =>{
    localStorage.setItem("token", token);
    localStorage.setItem("user", JSON.stringify(user));
    setToken(token);
    setAuthUser(user);
  }

  return (
    <BrowserRouter>
      {/* Truyền props vào navBar */}
      <Navbar authUser={authUser} onLogout={handleLogout} />

      {/* Thay thế inline style bằng className cho gọn gàng */}
      <div className="app-container">
        <Routes>
          <Route path="/" element={<HomePage authUser={authUser} />}/>
          {/*Đưa setToken cho LoginPage để nó báo*/}
          <Route path="/login" element={<LoginPage onLoginSuccess={handleLogin}/>}/>
          <Route path="/register" element={<RegisterPage/>}/>
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;