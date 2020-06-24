package com.rygf.controller;

import com.rygf.entity.Post;
import com.rygf.entity.Subject;
import com.rygf.service.PostService;
import com.rygf.service.SubjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Slf4j
//...
@Controller
public class HomeController {
    
    private final PostService postService;
    private final SubjectService subjectService;
    
    @GetMapping("/")
    public String showHomePage(Model model) {
        return "redirect:/1";
    }
    
    @GetMapping("/{curPage}")
    public String showHomePageWithPagination(
            @PathVariable("curPage") int curPage,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @RequestParam(value = "orderDir", required = false) String orderDir,
            Model model) {
        Page<Subject> subjects = subjectService.findAllLimitTo(9);
        model.addAttribute("subjects", subjects.getContent());
    
        Page<Post> postsPaginated = postService.findAllPaginated(curPage, orderBy, orderDir);

        model.addAttribute("posts", postsPaginated.getContent());
        model.addAttribute("curPage", curPage);
        model.addAttribute("totalPages", postsPaginated.getTotalPages());
        model.addAttribute("totalItems", postsPaginated.getTotalElements());
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("orderDir", orderDir);
        return "main";
    }
    
    
    @PreAuthorize("hasAuthority('POST_READ')")
    @GetMapping("/posts/{id}-{slug}")
    public String showPostDetail(@PathVariable("id") Long id, Model model,
            @PathVariable(value = "slug", required = false) String slug) {
        Post post = postService.find(id);
        model.addAttribute("post", post);
        return "post/single";
    }
    
    @GetMapping("/users/{id}-{slug}/posts")
    public String showUserPost(@PathVariable("id") Long id, Model model,
        @PathVariable(value = "slug", required = false) String slug) {
        List<Post> posts = postService.findByUser(id);
        model.addAttribute("posts", posts);
        return "user/display_post";
    }
    
    @GetMapping("/subjects/{id}-{slug}")
    public String showSubjectDetail(@PathVariable("id") Long id, Model model,
        @PathVariable(value = "slug", required = false) String slug) {
        Subject subject = subjectService.find(id);
        model.addAttribute("subject", subject);
        return "subject/display";
    }
    
}
