import React, {useState} from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

export function RegisterPage(){
    //tạo 4 state để lưu 4 thông tin từ from
    //useState(Hook): bộ nhớ của Component
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    //const[cái chứa dl, cái để đặt dl vào] = useState("")
    
    //gọi khi người dùng nhấn "Đăng ký"
    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault() //ngăn trình duyệt tự reload trang
        
        //Tạo object JavaScript, tên các key trùng với các key trong DTO: RegisterRequest.java
        const registerData = {
            username: username,
            password: password,
            name: name,
            email: email
        }

        try{
            //await nghĩa là chạy lệnh này đi, đợi Back-end trả lời rồi làm tiếp
            const response = await axios.post( //axios.post: gửi yêu cầu POST đến URL dưới kia và mang theo registerData
                "http://localhost:8080/api/auth/register", //địa chỉ API
                registerData
            );

            console.log("Đăng ký thành công!", response.data);
            alert("Đăng ký thành công! Đăng nhập Yuno ngay!");
        
        } catch (error){
            console.log("Lỗi khi đăng ký:", error);
            alert("Đăng ký thất bại! Hãy thử lại.");   
        }
    };
    return(
        // Thêm className cho container
        <div className="auth-container">
            <form className="auth-form" onSubmit={handleSubmit}>
                <h2>Tạo tài khoản Yuno</h2>
                <div className="form-group">
                    <label>Tên hiển thị</label>
                    <input type="text" value={name} onChange={(e) => setName(e.target.value)}/>
                </div>
                <div className="form-group">
                    <label>Tên đăng nhập</label>
                    <input 
                        type="text"
                        value={username} //(one-way-binding) đây là cái giá trị username ở useState trên kia
                        onChange={(e) => setUsername(e.target.value)} //khi gõ thì thêm chữ mới vào username
                    //   khi gõ phím      đặt cái      ký tự vừa gõ     vào username  (two-way-binding)
                    />
                </div>
                <div className="form-group">
                    <label>Mật khẩu</label>
                    {/* Sửa type="text" -> "password" cho an toàn */}
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)}/>
                </div>
                <div className="form-group">
                    <label>Địa chỉ Email</label>
                    {/* Sửa type="text" -> "email" cho đúng ngữ nghĩa */}
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)}/>
                </div>
                <button type='submit' className="auth-button">Đăng ký</button>
            </form>
            {/* Thêm className cho link chuyển đổi */}
            <p className="auth-switch-link">
                Đã có tài khoản? <Link to="/login">Đăng nhập ngay</Link>
            </p>
        </div>
    );
}