package com.rygf.controller;

import static com.rygf.common.ViewName.ACCOUNT_ANNOUNCE_VIEW;
import static com.rygf.common.ViewName.FORGET_PASSWORD_VIEW;
import static com.rygf.common.ViewName.LOGIN_VIEW;
import static com.rygf.common.ViewName.REGISTER;
import static com.rygf.common.ViewName.RESET_PASSWORD_VIEW;
import static com.rygf.common.ViewName.USER_SETTING_CHANGE_PASSWORD_VIEW;
import static com.rygf.common.ViewName.USER_SETTING_PROFILE;

import com.rygf.common.ImageUploader;
import com.rygf.dto.RegisterDTO;
import com.rygf.dto.UserPasswordDTO;
import com.rygf.dto.UserProfileDTO;
import com.rygf.exception.ImageException;
import com.rygf.exception.UserSettingException;
import com.rygf.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
public class AuthController {
    
    private final UserService userService;
    private final ImageUploader imageUploader;
    
    @Value("${profile_thumb.upload.path}")
    private String uploadPath;
    
    private static String generateServerURL(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append("http://");
        builder.append(request.getServerName() + ":" + request.getServerPort());
        builder.append(request.getContextPath());
        return builder.toString();
    }
    
    /*
     *   REGISTER
     * */
    
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDTO());
        return REGISTER;
    }
    
    @PostMapping("/register")
    public String processRegisterForm(@Valid @ModelAttribute("register") RegisterDTO registerDTO,
        BindingResult rs,
        HttpServletRequest request,
        Model model) {
        if (rs.hasErrors()) {
            return REGISTER;
        }
        
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
        return ACCOUNT_ANNOUNCE_VIEW;
    }
    
    @GetMapping("/registrationConfirm")
    public String confirmVerificationToken(@RequestParam(value = "token") String token,
        Model model) {
        userService.verifyRegistrationToken(token);
        
        model.addAttribute("heading", "Account Confirmation");
        model.addAttribute("content",
            "Your account has been activated!\n</br>Welcome to our family <3");
        return ACCOUNT_ANNOUNCE_VIEW;
    }
    
    
    /*
     *   LOGIN
     * */
    @GetMapping("/login")
    public String showLoginForm() {
        return LOGIN_VIEW;
    }
    
    /*
     *   FORGET PASSWORD
     * */
    @GetMapping("/forgetPassword")
    public String showRepasswordForm() {
        return FORGET_PASSWORD_VIEW;
    }
    
    @PostMapping("/forgetPassword")
    public String processSendResetPasswordToken(@RequestParam("email") String email,
        HttpServletRequest request, Model model) {
        userService.findByEmail(email);
        
        String serverURL = generateServerURL(request);
        userService.sendResetPasswordToken(email, serverURL);
        
        model.addAttribute("heading", "Send Token Success");
        model.addAttribute("content", "ResetToken has been sent to email : " + email);
        return ACCOUNT_ANNOUNCE_VIEW;
    }
    
    @GetMapping("/resetPasswordConfirm")
    public String confirmResetPasswordToken(@RequestParam(value = "token") String token,
        Model model) {
        userService.verifyResetPasswordToken(token);
        return RESET_PASSWORD_VIEW;
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/resetPassword")
    public String resetPasswordProcess(@RequestParam("password") String password) {
        userService.resetPassword(password);
        return "redirect:/";
    }
    
    /*
     ************************************
     *   SETTINGS
     ************************************
     */
    
    // settings PROFILE
    @GetMapping("/settings/profile")
    @PreAuthorize("isAuthenticated()")
    public String showChangeInfoForm(Model model) {
        model.addAttribute("profile", userService.findProfileDto());
        return USER_SETTING_PROFILE;
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/settings/profile/submit")
    public String showChangeInfoForm(@Valid @ModelAttribute("profile") UserProfileDTO profile,
        BindingResult rs) {
        if (rs.hasErrors()) {
            return USER_SETTING_PROFILE;
        }
        
        MultipartFile source = profile.getThumbnail();
        if (source == null || source.isEmpty() || source.getOriginalFilename().isBlank()) {
            userService.updateProfile(profile);
            return "redirect:/";
        }
        try {
            userService.deleteExistThumbnail();
            String finalDesFileName = imageUploader.uploadFile(source, uploadPath);
            profile.setFinalDesFileName(finalDesFileName);
        } catch (ImageException e) {
            rs.rejectValue("thumbnail", null, e.getMessage());
            return USER_SETTING_PROFILE;
        }
        
        userService.updateProfile(profile);
        return "redirect:/";
    }
    
    // Settings PASSWORD
    @GetMapping("/settings/password")
    @PreAuthorize("isAuthenticated()")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("profile", new UserPasswordDTO());
        return USER_SETTING_CHANGE_PASSWORD_VIEW;
    }
    
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/settings/password/submit")
    public String showChangeInfoForm(@Valid @ModelAttribute("profile") UserPasswordDTO profile,
        BindingResult rs) {
        if (rs.hasErrors()) {
            return USER_SETTING_CHANGE_PASSWORD_VIEW;
        }
        
        try {
            userService.changePassword(profile);
        } catch (UserSettingException e) {
            rs.rejectValue("oldPassword", null, e.getMessage());
            return USER_SETTING_CHANGE_PASSWORD_VIEW;
        }
        
        return "redirect:/";
    }
    
}
