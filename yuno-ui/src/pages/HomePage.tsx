import { useState, useEffect } from "react";
import axios from "axios";

//định nghĩa khuôn Interface cho Post
interface Post{
    id: number;
    content: string;
    user_id: number;
    user: {
        id: number;
        username: string;
        name: string;
    } | null
}

export function HomePage() {
    //tạo STATE để lưu ds bài đăng, ban đầu là rỗng []
    const [posts, setPosts] = useState<Post[]>([]);
    const [content, setContent] =  useState("");
    
    const fetchPosts = async () =>{
        try{
            //gọi API công khai (ko cần token)
            const response = await axios.get("http://localhost:8080/api/posts");
            //Đặt ds post lấy về vào STATE
            setPosts(response.data);
        } catch (error){
            console.error("Lỗi khi lấy bài đăng: ", error);
        }
    };

    //code bên trong này sẽ tự động chạy 1 lần ngay sau khi HomePage hiển thị
    useEffect(() => {
        fetchPosts(); //chạy hàm "gọi món"
    }, []); //Dấu [] nghĩa là chỉ chạy 1 lần lúc tải trang

    const handleSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        const token = localStorage.getItem("token");

        if (!token){
            alert("Bạn chưa đăng nhập! Vui lòng đăng nhập để đăng bài.")
            return;
        }

        const postData = {
          content: content  
        };
        
        try{
            //gọi API và xuất trình token
            const response = await axios.post(
                "http://localhost:8080/api/posts", //cửa API
                postData, //Form (body)
                {
                    //xuất trình token (header)
                    headers: {
                        'Authorization': `Bearer ${token}` //Kèm token vào tiêu đề
                    }
                }
            );

            alert("Bài viết đã được đăng tải");
            setContent(""); //xóa chữ trong form
            fetchPosts(); //reload danh sách post (bảng tin)

        } catch (error){
            console.log("Lỗi khi đăng bài: ", error);
            alert("Đăng bài thất bại, hãy thử lại!");
        }
    }

    //giao diện
    return(
        // Thêm className cho container của HomePage
        <div className="homepage-container">
            
            {/* Đổi tên className cho "xịn" */}
            <div className="create-post-container">
                <h3>Tạo bài đăng mới</h3>
                <form className="create-post-form" onSubmit={handleSubmit}>
                    <textarea
                        value={content} placeholder="Bạn đang nghĩ gì?"
                        onChange={(e) => setContent(e.target.value)}
                    ></textarea>
                    {/* Thêm className cho nút "Đăng bài" */}
                    <input type="submit" value="Đăng bài" className="post-submit-button" />
                </form>
            </div>

            {/* Thêm className cho đường kẻ ngang */}
            <hr className="divider" />

            <div className="post-list-container">
                <h3>Dòng thời gian</h3>
                {/*Lặp (MAP) qua danh sách post trong STATE*/}
                {posts.slice().reverse().map(post => (
                  // "key={post.id} là bắt buộc, để React biết phân biệt"
                  // Thay thế inline style bằng className
                  <div key={post.id} className="post-card">
                    {/* Thêm className cho header của post */}
                    <div className="post-header">
                        <strong>{post.user ? post.user.name: 'User (ID: ${post.user_id})'}</strong>
                    </div>
                    {/* Thêm className cho nội dung post */}
                    <p className="post-content">{post.content}</p>
                  </div>
                ))}
            </div>
        </div>
    ); 
}