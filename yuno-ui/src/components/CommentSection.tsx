// File: src/components/CommentSection.tsx
import { useState, useEffect } from "react";
import axios from "axios";
import { FaPaperPlane } from "react-icons/fa";
const PaperPlane = FaPaperPlane as React.ElementType;


//Định nghĩa "Khuôn" (Interface) cho Comment
interface Comment {
    id: number;
    content: string;
    user: {
        id: number;
        username: string;
        name: string;
    } | null;
}

//Nó cần biết nó đang hiển thị cho bài post nào
interface CommentSectionProps {
    postId: number; //Cần post ID
}

export function CommentSection({ postId }: CommentSectionProps) {
    
    const [comments, setComments] = useState<Comment[]>([]); //State lưu danh sách comment
    const [newComment, setNewComment] = useState(""); //State lưu chữ đang gõ

    //Hàm tải Comment
    //Chạy 1 lần khi component này hiện ra
    useEffect(() => {
        const fetchComments = async () => {
            try {
                //Call cửa (API) GET ở Back-end
                const response = await axios.get(`http://localhost:8080/api/comments/by-post/${postId}`);
                setComments(response.data); //Bỏ vào state
            } catch (error) {
                console.error("Lỗi khi lấy (fetch) comments:", error);
            }
        };

        fetchComments();
    }, [postId]); //Dấu [postId] nghĩa là: "Nếu postId thay đổi, tải lại!

    //Gửi Comment MỚI
    const handleCommentSubmit = async (event: React.FormEvent) => {
        event.preventDefault();
        const token = localStorage.getItem("token");
        if (!token) {
            alert("Bạn phải đăng nhập để bình luận!");
            return;
        }

        try {
            //Gói (Package) "Form" (DTO)
            const commentData = {
                content: newComment,
                postId: postId //Lấy từ "props" ở trên
            };

            //Call (API) POST ở Back-end
            const response = await axios.post(
                "http://localhost:8080/api/comments",
                commentData,
                { headers: { 'Authorization': `Bearer ${token}` } }
            );

            //làm  mớilist
            //thêm cái comment MỚI vừa trả về vào đầu list hiện tại
            setComments([response.data, ...comments]);
            setNewComment(""); //clear chữ trong textbox

        } catch (error) {
            console.error("Lỗi khi gửi (submit) comment:", error);
            alert("Bình luận thất bại!");
        }
    };

    return (
        <div className="comment-section">
            {/*FORM ĐỂ TẠO COMMENT MỚI*/}
            <form className="comment-form" onSubmit={handleCommentSubmit}>
                <textarea
                    value={newComment}
                    onChange={(e) => {
                        setNewComment(e.target.value);
                        e.target.style.height = 'auto';
                        e.target.style.height = e.target.scrollHeight + 'px';
                    }}
                    placeholder="Viết bình luận..."
                    rows={1}
                />
                <button type="submit" className="comment-submit-button" title="Gửi bình luận">
                        <PaperPlane />
                </button>
            </form>

            {/*DANH SÁCH COMMENT CŨ*/}
            <div className="comment-list">
                {comments.slice().reverse().map(comment => (
                    <div key={comment.id} className="comment-card">
                        <strong>{comment.user ? comment.user.name : "User ẩn danh"}</strong>
                        <p>{comment.content}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}