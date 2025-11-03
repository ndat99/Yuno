package com.yuno.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.yuno.api.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{

    
}
