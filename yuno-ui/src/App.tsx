// File: src/App.tsx
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { HomePage } from "./pages/HomePage";
import { LoginPage } from "./pages/LoginPage";
import { RegisterPage } from "./pages/RegisterPage";
import { Navbar } from "./components/Navbar";
import { useState } from "react";

function App() {
  //lấy token từ localStorage để làm giá trị ban đầu
  const [token, setToken] = useState(localStorage.getItem("token"));
 
  return (
    <BrowserRouter>
      {/* Navbar sẽ dùng "token" và "setToken" */}
      <Navbar token={token} setToken={setToken} />

      {/* Thay thế inline style bằng className cho gọn gàng */}
      <div className="app-container">
        <Routes>
          <Route path="/" element={<HomePage />}/>
          {/*Đưa setToken cho LoginPage để nó báo*/}
          <Route path="/login" element={<LoginPage setToken={setToken}/>}/>
          <Route path="/register" element={<RegisterPage/>}/>
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;