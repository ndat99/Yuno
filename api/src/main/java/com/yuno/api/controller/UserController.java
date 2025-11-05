package com.yuno.api.controller;

import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.yuno.api.service.LikeService;

@RestController
@RequestMapping("/api/me")
public class UserController {
    private final LikeService likeService;

    public UserController(LikeService likeService){
        this.likeService = likeService;
    }

    @GetMapping("/likes")
    public ResponseEntity<Set<Integer>> getMyLikes(Authentication authentication){
        //lấy "ai" từ token
        String username = authentication.getName();
        //Giao cho LikeService
        Set<Integer> myLikedPostIds = likeService.getMyLikedPostIds(username);

        return ResponseEntity.ok(myLikedPostIds);
    }
}
