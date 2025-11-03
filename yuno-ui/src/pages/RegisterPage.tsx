import React, {useState} from 'react';
import axios from 'axios';

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
        <div>
            <h2>Tạo tài khoản Yuno ngay!</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Username:</label>
                    <input 
                        type="text"
                        value={username} //(one-way-binding) đây là cái giá trị username ở useState trên kia
                        onChange={(e) => setUsername(e.target.value)} //khi gõ thì thêm chữ mới vào username
                    //   khi gõ phím      đặt cái      ký tự vừa gõ     vào username  (two-way-binding)
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input type="text" value={password} onChange={(e) => setPassword(e.target.value)}/>
                </div>
                <div>
                    <label>Name:</label>
                    <input type="text" value={name} onChange={(e) => setName(e.target.value)}/>
                </div>
                <div>
                    <label>Email:</label>
                    <input type="text" value={email} onChange={(e) => setEmail(e.target.value)}/>
                </div>
                <button type='submit'>Đăng ký</button>
            </form>
        </div>
    );
}