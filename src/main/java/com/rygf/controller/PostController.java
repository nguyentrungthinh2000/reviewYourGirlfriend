package com.rygf.controller;

import com.rygf.dto.CrudStatus;
import com.rygf.dto.CrudStatus.STATUS;
import com.rygf.dto.PostDTO;
import com.rygf.exception.ImageException;
import com.rygf.service.PostService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/dashboard/post")
@Controller
@Slf4j
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @ModelAttribute("crudStatus")
    public CrudStatus getCrudStatus() {
        return new CrudStatus();
    }
    
    @GetMapping
    public String showPostDashboard() {
        return "post/dashboard";
    }
    
    @GetMapping("/create")
    public String showPostForm(@ModelAttribute("post")PostDTO postDTO) {
        return "post/form";
    }
    
    @PostMapping("/submit")
    public String processForm(@Valid @ModelAttribute("post")PostDTO postDTO,
        BindingResult rs,
        RedirectAttributes ra
    ) {
        if(rs.hasErrors()) {
            return "post/form";
        }
    
        MultipartFile source = postDTO.getThumbnail();
        try {
            String finalDesFileName = postService.uploadFile(source);
            postDTO.setFinalDesFileName(finalDesFileName);
        } catch (ImageException e) {
            rs.rejectValue("thumbnail", null, e.getMessage());
            return "post/form";
        }
        
        postService.createOrUpdate(postDTO);
        ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.CREATE_SUCCESS));
        return "redirect:/dashboard/post";
    }
    
}
