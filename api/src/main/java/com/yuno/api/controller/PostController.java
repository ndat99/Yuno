package com.yuno.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yuno.api.dto.CreatePostRequest;
import com.yuno.api.model.Post;
import com.yuno.api.service.PostService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    public PostController(PostService postService){
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(){
        //gọi service
        List<Post> posts = postService.getAllPosts();
        //trả về danh sách post
        return ResponseEntity.ok(posts);
    }
    
}
