package com.rygf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String showHomePage() {
        return "main";
    }
    
    @GetMapping("/post/{id}")
    public String showPostDetail(@PathVariable("id") Long id) {
        return "single";
    }
}
