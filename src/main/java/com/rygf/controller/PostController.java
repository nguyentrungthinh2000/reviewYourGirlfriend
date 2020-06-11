package com.rygf.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/dashboard/post")
@Controller
public class PostController {
    
    @ModelAttribute("crudStatus")
    public Map<String, String> getCrudStatus() {
        HashMap<String, String> map = new HashMap<>();
        map.put("message", null);
        map.put("cssClass", null);
        return map;
    }
    
    @GetMapping
    public String showPostDashboard() {
        return "post/dashboard";
    }
    
    @GetMapping("/create")
    public String showPostForm() {
        return "post/form";
    }
}
