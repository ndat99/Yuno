package com.yuno.api.service;

import java.util.List;
import com.yuno.api.model.User;
import org.springframework.stereotype.Service;
import com.yuno.api.ApiApplication;
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

    public List<Post> getAllPosts(){
        return postRepository.findAll();
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
