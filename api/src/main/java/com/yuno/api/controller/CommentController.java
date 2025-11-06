package com.yuno.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yuno.api.service.CommentService;
import com.yuno.api.dto.CommentResponse;
import com.yuno.api.dto.CreateCommentRequest;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/comments")
public class CommentController {
    
    private final CommentService commentService;

    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentResponse>> getCommentsByPost(@PathVariable int postId) {
        List<CommentResponse> comments = commentService.getCommentForPost(postId);
        return ResponseEntity.ok(comments);
    }
    
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@RequestBody CreateCommentRequest dto, Authentication auth){
        String username = auth.getName();
        CommentResponse createdComment = commentService.createComment(dto, username);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);   
    }
}
