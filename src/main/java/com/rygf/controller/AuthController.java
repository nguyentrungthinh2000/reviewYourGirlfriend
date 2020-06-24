package com.rygf.controller;

import com.rygf.dto.RegisterDTO;
import com.rygf.dto.UserPasswordDTO;
import com.rygf.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class AuthController {
    
    private final UserService userService;
    
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDTO());
        return "register";
    }
    
    @PostMapping("/register")
    public String processRegisterForm(@Valid @ModelAttribute("register") RegisterDTO registerDTO,
            BindingResult rs) {
        if(rs.hasErrors())
            return "register";
        
        userService.register(registerDTO);
        return "redirect:/";
    }
    
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }
    
    @GetMapping("/settings/profile")
    @PreAuthorize("isAuthenticated()")
    public String showChangeInfoForm(Model model) {
        model.addAttribute("profile", userService.findProfileDto());
        return "user/profile";
    }
    
    @GetMapping("/settings/password")
    @PreAuthorize("isAuthenticated()")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("profile", new UserPasswordDTO());
        return "user/change_password";
    }
    
}
