package com.rygf.controller;

import com.rygf.dto.RegisterDTO;
import com.rygf.dto.UserPasswordDTO;
import com.rygf.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class AuthController {
    
    private final UserService userService;
    
    private static String generateServerURL(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append("http://");
        builder.append(request.getServerName() + ":" + request.getServerPort());
        builder.append(request.getContextPath());
        return builder.toString();
    }
    
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDTO());
        return "register";
    }
    
    @PostMapping("/register")
    public String processRegisterForm(@Valid @ModelAttribute("register") RegisterDTO registerDTO,
            BindingResult rs,
            HttpServletRequest request,
            Model model) {
        if(rs.hasErrors())
            return "register";
    
        String serverURL = generateServerURL(request);
        userService.register(registerDTO, serverURL);
        
        // Announce
        model.addAttribute("heading", "Account Verification");
        StringBuilder builder = new StringBuilder();
        builder.append("Last step to finish & activate your account</br>");
        builder.append("We've already send verification token</br>To email :  ");
        builder.append("<u>");
        builder.append(registerDTO.getEmail());
        builder.append("</u>");
        model.addAttribute("content", builder.toString());
        return "account_announce";
    }
    
    @GetMapping("/registrationConfirm")
    public String confirmVerificationToken(@RequestParam(value = "token") String token, Model model) {
        userService.verifyRegistrationToken(token);
        
        model.addAttribute("heading", "Account Confirmation");
        model.addAttribute("content", "Your account has been activated!\n</br>Welcome to our family <3");
        return "account_announce";
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
    
    @GetMapping("/forgetPassword")
    public String showRepasswordForm() {
        return "forget-password";
    }
    
    @PostMapping("/forgetPassword")
    public String processSendResetPasswordToken(@RequestParam("email")String email,
        HttpServletRequest request, Model model) {
        userService.findByEmail(email);
        
        String serverURL = generateServerURL(request);
        userService.sendResetPasswordToken(email, serverURL);
        
        model.addAttribute("heading", "Send Token Success");
        model.addAttribute("content", "ResetToken has been sent to email : " + email);
        return "account_announce";
    }
    
    @GetMapping("/resetPasswordConfirm")
    public String confirmResetPasswordToken(@RequestParam(value = "token") String token, Model model) {
        userService.verifyResetPasswordToken(token);
        return "reset-password";
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/resetPassword")
    public String resetPasswordProcess(@RequestParam("password")String password) {
        userService.resetPassword(password);
        return "redirect:/";
    }
    
}
