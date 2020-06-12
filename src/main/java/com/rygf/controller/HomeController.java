package com.rygf.controller;

import com.rygf.entity.Post;
import com.rygf.service.PostService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
    
    @Autowired
    private PostService postService;
    
    @GetMapping("/")
    public String showHomePage(Model model) {
        List<Post> posts = postService.findAll();
        model.addAttribute("posts", posts);
        
        return "main";
    }
    
    @GetMapping("/post/{id}")
    public String showPostDetail(@PathVariable("id") Long id, Model model) {
        Post post = postService.find(id);
        model.addAttribute("post", post);
        return "post/single";
    }
    
}
