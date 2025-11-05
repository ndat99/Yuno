package com.yuno.api.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.yuno.api.model.Like;
import com.yuno.api.model.Post;
import com.yuno.api.model.User;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer>{
    Optional<Like> findByUserAndPost(User user, Post post);
    int countByPost_id(int postId);
    List<Like> findAllByUser(User user);
}