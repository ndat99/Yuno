package com.yuno.api.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.yuno.api.model.User;
import com.yuno.api.model.Like;
import com.yuno.api.model.Post;
import com.yuno.api.repository.LikeRepository;
import com.yuno.api.repository.PostRepository;
import com.yuno.api.repository.UserRepository;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikeService(UserRepository userRepository, PostRepository postRepository, LikeRepository likeRepository){
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }

    public void toggledLike(int postId, String username){
        Optional<User> userOptional = userRepository.findByUsername(username);
        Optional<Post> postOptional = postRepository.findById(postId);
        
        
        if (userOptional.isPresent() && postOptional.isPresent()){
            User user = userOptional.get();
            Post post = postOptional.get();
            
            Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);
            
            if (existingLike.isPresent()) {
                likeRepository.delete(existingLike.get());
            }
            else{
                Like newLike = new Like();
                newLike.setUser_id(user.getId());
                newLike.setPost_id(post.getId());
                
                likeRepository.save(newLike);
            }
        }
        else{
            throw new RuntimeException("Không tìm thấy User hoặc Post.");
        }
    }

    public Set<Integer> getMyLikedPostIds(String username){
        User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng: " + username));

        List<Like> myLikes = likeRepository.findAllByUser(user);

        return myLikes.stream() //biến List thành dòng chảy
                    .map(like -> like.getPost_id()) //với mỗi Like, chỉ lấy post_id
                    .collect(Collectors.toSet()); //Gom chúng lại thành 1 Set (ko trùng lặp) 
    }
}
