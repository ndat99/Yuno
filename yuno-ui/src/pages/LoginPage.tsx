import React, {useState} from "react";
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";

export function LoginPage({setToken}: {setToken: (token: string | null) => void}) {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    
    // (Sửa lỗi chính tả navaigate -> navigate)
    const navigate = useNavigate();

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();

        const loginData = {
            username: username,
            password: password
        };

        try{
            const response = await axios.post(
                "http://localhost:8080/api/auth/login",
                loginData
            );
            
            const token = response.data.token; //lấy token ra
            localStorage.setItem("token", token); //bỏ vào "ví" của trình duyệt
            setToken(token); //báo cho App.tsx biết
            //để ko bị mất dl khi refresh trang
            navigate("/"); // (Sửa lỗi chính tả)
        } catch(error){
            console.log("Đăng nhập thất bại, vui lòng thử lại!");
            alert("Đăng nhập thất bại! Vui lòng thử lại.");
        }
    }
    return(
        // Thêm className cho container
        <div className="auth-container">
            <form className="auth-form" onSubmit={handleSubmit}>
                <h2>Đăng nhập</h2>

                <div className="form-group">
                    <label>Tên đăng nhập</label>
                    <input type="text" value={username} onChange={(e) => setUsername(e.target.value)}/>
                </div>

                <div className="form-group">
                    <label>Mật khẩu</label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)}/>
                </div>
                
                <button type="submit" className="auth-button">Đăng nhập</button>
            </form>

            {/* Thêm className cho link chuyển đổi */}
            <p className="auth-switch-link">
                Chưa có tài khoản? <Link to="/register">Đăng ký ngay</Link>
            </p>
        </div>
    );
}