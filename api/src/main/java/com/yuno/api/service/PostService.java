package com.yuno.api.service;

import java.util.List;
import java.util.stream.Collectors;
import com.yuno.api.model.User;
import org.springframework.stereotype.Service;
import com.yuno.api.ApiApplication;
import com.yuno.api.dto.PostResponse;
import com.yuno.api.dto.UserResponse;
import com.yuno.api.model.Post;
import com.yuno.api.repository.PostRepository;
import com.yuno.api.repository.UserRepository;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, ApiApplication apiApplication){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public List<PostResponse> getAllPosts(){
        //lấy danh sách "khuôn" (Post) từ CSDL
        List<Post> posts = postRepository.findAll();
        //chuyển (map) thành danh sách đó
        return posts.stream() //biến danh sách thành một "dòng chảy"
                .map(this::mapToPostResponse) //với mỗi post, hãy gọi hàm mapToPostResponse
                .collect(Collectors.toList()); //gom "dòng chảy" lại thành List mới
    }

    //hepler: chuyển đổi
    private PostResponse mapToPostResponse(Post post){
        //tạo "túi user" con
        UserResponse userResponse = new UserResponse();
        userResponse.setId(post.getUser().getId());
        userResponse.setUsername(post.getUser().getUsername());
        userResponse.setName(post.getUser().getName());

        //tạo "túi post" chính
        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setContent(post.getContent());
        postResponse.setUser(userResponse);

        return postResponse;
    }

    public Post createPost(String content, String username){
        //Dùng username lấy từ token để tìm User
        User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        
        //Tạo model Post mới
        Post newPost = new Post();
        newPost.setContent(content);
        //Gắn user_id vào bài post
        newPost.setUser_id(user.getId());
        //Lưu bài post (đã có chủ) xuống CSDL
        return postRepository.save(newPost);
    }
}
