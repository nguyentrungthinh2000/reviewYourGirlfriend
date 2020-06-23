package com.rygf.dao;

import com.rygf.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    
    @Query("SELECT p FROM Post p WHERE p.author.id = :userId")
    List<Post> findByUser(@Param("userId") Long userId);
    
}
