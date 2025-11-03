import React, { useState } from "react";
import axios from 'axios';
import { useNavigate } from "react-router-dom";
import styles from './LoginPage.module.css'; // Import CSS Modules

// Giả sử bạn lưu logo dưới dạng SVG hoặc PNG trong thư mục public
// và có thể import nó hoặc dùng đường dẫn trực tiếp
// import YunoLogo from './yuno-logo.svg'; // Nếu là file SVG

export function LoginPage() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState<string | null>(null); // State để lưu thông báo lỗi
    
    const navigate = useNavigate(); // Đã sửa lỗi chính tả từ navaigate -> navigate

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        setError(null); // Xóa lỗi cũ khi submit lại

        const loginData = {
            username: username,
            password: password
        };

        try {
            const response = await axios.post(
                "http://localhost:8080/api/auth/login",
                loginData
            );
            
            const token = response.data.token;
            localStorage.setItem("token", token);
            navigate("/"); // Điều hướng về trang chủ
        } catch (err) {
            console.error("Đăng nhập thất bại:", err); // Ghi log lỗi chi tiết
            setError("Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại!"); // Hiển thị lỗi thân thiện
            // alert("Đăng nhập thất bại! Vui lòng thử lại."); // Không dùng alert vì nó chặn UI
        }
    }

    return (
        <div className={styles.loginContainer}>
            <div className={styles.loginBox}>
                <div className={styles.logo}>
                    {/* Sử dụng logo đã tạo. Bạn có thể thay đường dẫn này bằng URL của logo hoặc import */}
                    <img src="https://i.imgur.com/your-yuno-logo.png" alt="Yuno Logo" /> 
                    {/* <div className={styles.logoText}>Yuno</div> */}
                </div>
                <div className={styles.tagline}>Connect. Share. Discover.</div>
                <h2>Đăng nhập vào Yuno</h2>
                <form onSubmit={handleSubmit}>
                    <div className={styles.formGroup}>
                        <label htmlFor="username">Tên đăng nhập</label>
                        <input 
                            type="text" 
                            id="username" 
                            value={username} 
                            onChange={(e) => setUsername(e.target.value)} 
                            required 
                        />
                    </div>
                    <div className={styles.formGroup}>
                        <label htmlFor="password">Mật khẩu</label>
                        <input 
                            type="password" 
                            id="password" 
                            value={password} 
                            onChange={(e) => setPassword(e.target.value)} 
                            required 
                        />
                    </div>
                    {error && <div className={styles.errorMessage}>{error}</div>} {/* Hiển thị lỗi */}
                    <button type="submit" className={styles.submitButton}>Đăng nhập</button>
                </form>
                <div className={styles.registerLink}>
                    Bạn chưa có tài khoản? <a href="/register">Đăng ký ngay</a>
                </div>
            </div>
        </div>
    );
}