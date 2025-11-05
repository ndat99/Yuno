package com.yuno.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yuno.api.dto.CreatePostRequest;
import com.yuno.api.dto.PostResponse;
import com.yuno.api.model.Like;
import com.yuno.api.model.Post;
import com.yuno.api.service.LikeService;
import com.yuno.api.service.PostService;
import java.net.Authenticator;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    private final PostService postService;
    private final LikeService likeService;

    public PostController(PostService postService, LikeService likeService){
        this.postService = postService;
        this.likeService = likeService;
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts(){
        //gọi service
        List<PostResponse> posts = postService.getAllPosts();
        //trả về danh sách post
        return ResponseEntity.ok(posts);
    }

    //thêm cửa API tạo bài đăng mới
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody CreatePostRequest postRequest, Authentication authentication) {
        //lấy username từ token (đã được Security xác thực)
        String username = authentication.getName();
        //gọi quản lý (Service)
        PostResponse createdPost = postService.createPost(
            postRequest.getContent(), //lấy content từ Form
            username                  //lấy username từ token
        );
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable int postId, Authentication authentication){
        try{
            //lấy "ai" từ token
            String username = authentication.getName();
            //giao việc cho Service
            likeService.toggledLike(postId, username);
            return ResponseEntity.ok().build();
        } catch(RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }    
    
    
}
