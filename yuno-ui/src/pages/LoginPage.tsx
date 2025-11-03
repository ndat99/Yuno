import React, {useState} from "react";
import axios from 'axios';
import { useNavigate } from "react-router-dom";

export function LoginPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    
    const navaigate = useNavigate();

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
            //để ko bị mất dl khi refresh trang
            navaigate("/");
        } catch(error){
            console.log("Đăng nhập thất bại, vui lòng thử lại!");
            alert("Đăng nhập thất bại! Vui lòng thử lại.");
        }
    }
    return(
        <div>
            <h2>Đăng nhập vào Yuno</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Username</label>
                    <input type="text" value={username} onChange={(e) => setUsername(e.target.value)}/>
                </div>3
                <div>
                    <label>Password</label>
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)}/>
                </div>
                <button type="submit">Đăng nhập</button>
            </form>
        </div>
    );
}