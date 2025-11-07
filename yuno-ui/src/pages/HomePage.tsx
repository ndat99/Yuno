import { useState, useEffect } from "react";
import axios from "axios";
import { FaRegHeart, FaHeart, FaRegComment } from "react-icons/fa";
import { CommentSection } from "../components/CommentSection";

const Heart = FaHeart as React.ElementType;
const HeartOutline = FaRegHeart as React.ElementType;
const Comment = FaRegComment as React.ElementType;

//định nghĩa khuôn Interface cho Post
interface Post{
    id: number;
    content: string;
    user_id: number;
    user: {
        id: number;
        username: string;
        name: string;
    } | null;
    likeCount: number;
    commentCount: number;
}

export function HomePage() {
    //tạo STATE để lưu ds bài đăng, ban đầu là rỗng []
    const [posts, setPosts] = useState<Post[]>([]);
    //tạo STATE để lưu nội dung bài đăng mới
    const [content, setContent] =  useState("");
    //tạo STATE để lưu những bài đã like
    const [myLikedPosts, setMyLikedPosts] = useState<Set<number>>(new Set());
    //tạo STATE để lưu những bài đã mở commment
    const [openCommentSections, setOpenCommentSections] = useState<Set<number>>(new Set());

    //lấy ds post
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

    //tra xem đã like post nào
    const fetchMyLikes = async () =>{
        const token = localStorage.getItem("token");
        if (!token) return;

        try{
            const response = await axios.get("http://localhost:8080/api/me/likes",
                { headers: {'Authorization': `Bearer ${token}`}
            });
            setMyLikedPosts(new Set(response.data));
        } catch (error){
            console.error("Lỗi khi lấy danh sách post đã like: ", error);
        }
    }

    //code bên trong này sẽ tự động chạy 1 lần ngay sau khi HomePage hiển thị
    useEffect(() => {
        fetchPosts(); //chạy hàm "gọi món"
        fetchMyLikes(); //chạy hàm "tra cứu like"
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

    const handleLikeToggle = async (postId: number) => {
        const token = localStorage.getItem("token");
        if (!token){
            alert("Bạn phải đăng nhập để thích bài viết!");
            return;
        }

        try{
            await axios.post(
                `http://localhost:8080/api/posts/${postId}/like`,
                {}, //ko cần body
                {headers: {'Authorization': `Bearer ${token}`}}
            );
            fetchPosts();
            fetchMyLikes();
        } catch (error){
            console.error("Lỗi khi thích bài viết: ", error);
            alert("Đã xảy ra lỗi khi tương tác với bài viết");
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
                        onChange={(e) => {
                            setContent(e.target.value);
                            e.target.style.height = 'auto';
                            e.target.style.height = e.target.scrollHeight + 'px';
                        }}
                        rows={1}
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
                {posts.slice().reverse().map(post => {
                    //Tra cứu liked post, kiểm tra xem myLikedPosts có chứa id này ko
                    const isLikedByMe = myLikedPosts.has(post.id);
                    return (
                    // "key={post.id} là bắt buộc, để React biết phân biệt"
                    <div key={post.id} className="post-card">
                        <div className="post-header">
                            <strong>{post.user ? post.user.name : `User (ID: ${post.user_id})`}</strong>
                        </div>
                        <p className="post-content">{post.content}</p>

                        <div className="post-actions">
                            <button
                                onClick={() => handleLikeToggle(post.id)}
                                className={isLikedByMe ? 'like-button liked' : 'like-button'}
                            >
                                {/* {isLikedByMe ? <FaHeart /> : <FaRegHeart />} */}
                                {isLikedByMe ? <Heart /> : <HeartOutline />}
                            </button>
                            <span className="like-count">{post.likeCount}</span>
                            <button className="comment-button"
                            onClick={() => {
                                const newSet = new Set(openCommentSections);
                                if (newSet.has(post.id)){
                                    //nếu đã có -> xóa nó đi (đóng)
                                    newSet.delete(post.id);
                                } else {
                                    //nếu chưa có -> thêm nó vào (mở)
                                    newSet.add(post.id);
                                }
                                //cập nhật bộ nhớ bằng bản sao mới
                                setOpenCommentSections(newSet);
                            }}
                            >
                                <Comment />
                            </button>
                            <span className="comment-count">{post.commentCount}</span>
                        </div>
                       {openCommentSections.has(post.id) && (
                            <CommentSection postId={post.id} />
                        )} 
                    </div>
                    );
                })}
            </div>
        </div>
    ); 
}