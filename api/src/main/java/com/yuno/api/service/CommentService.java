package com.yuno.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.yuno.api.dto.CommentResponse;
import com.yuno.api.dto.CreateCommentRequest;
import com.yuno.api.dto.UserResponse;
import com.yuno.api.model.Comment;
import com.yuno.api.model.Post;
import com.yuno.api.model.User;
import com.yuno.api.repository.CommentRepository;
import com.yuno.api.repository.PostRepository;
import com.yuno.api.repository.UserRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository){
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public List<CommentResponse> getCommentForPost(int postId){
        List<Comment> comments = commentRepository.findAllByPost_id(postId);
        return comments.stream()
                .map(this::mapToCommentResponse)
                .collect(Collectors.toList());
    }

    private CommentResponse mapToCommentResponse(Comment comment){
        UserResponse userResponse = new UserResponse();
        userResponse.setId(comment.getUser().getId());
        userResponse.setUsername(comment.getUser().getUsername());
        userResponse.setName(comment.getUser().getName());

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setContent(comment.getContent());
        commentResponse.setUser(userResponse);

        return commentResponse;
    }

    public CommentResponse createComment(CreateCommentRequest dto, String username){
        User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        Post post = postRepository.findById(dto.getPostId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));        
        
        Comment newComment = new Comment();
        newComment.setContent(dto.getContent());
        newComment.setUser_id(user.getId());
        newComment.setPost_id(post.getId());
        Comment savedComment = commentRepository.save(newComment);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setName(user.getName());

        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(savedComment.getId());
        commentResponse.setContent(savedComment.getContent());
        commentResponse.setUser(userResponse);

        return commentResponse;
    }
}
