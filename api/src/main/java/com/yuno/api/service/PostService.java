package com.yuno.api.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.yuno.api.model.Post;
import com.yuno.api.repository.PostRepository;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }
}
