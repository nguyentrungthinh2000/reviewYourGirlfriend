package com.rygf.exception.handler;

import com.rygf.exception.DuplicateEntityException;
import com.rygf.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileUploadSize;
    
    @ExceptionHandler(EntityNotFoundException.class)
    public String EntityNotFoundExcHandler(EntityNotFoundException e, Model model) {
        model.addAttribute("code", HttpStatus.NOT_FOUND.value());
        model.addAttribute("message", e.getMessage());
        return "error";
    }
    
    @ExceptionHandler(DuplicateEntityException.class)
    public String EntityNotFoundExcHandler(DuplicateEntityException e, Model model) {
        model.addAttribute("code", HttpStatus.CONFLICT.value());
        model.addAttribute("message", e.getMessage());
        return "error";
    }
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileSizeException(MaxUploadSizeExceededException e, Model model) {
        model.addAttribute("code", HttpStatus.INSUFFICIENT_STORAGE.value());
        model.addAttribute("message", "File has exceeded maximum upload size : " + maxFileUploadSize);
        return "error";
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException e, Model model) {
        model.addAttribute("code", HttpStatus.FORBIDDEN.value());
        model.addAttribute("message", e.getMessage());
        return "error";
    }
}
