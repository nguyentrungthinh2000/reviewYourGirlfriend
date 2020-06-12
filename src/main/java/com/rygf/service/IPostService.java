package com.rygf.service;

import com.rygf.dto.PostDTO;
import com.rygf.entity.Post;
import java.util.List;

public interface IPostService {
    
    void createOrUpdate(PostDTO post);
    Post find(Long id);
    List<Post> findAll();
    void delete(Long id);
    
}
