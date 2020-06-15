package com.rygf.exception.handler;

import com.rygf.exception.DuplicateEntityException;
import com.rygf.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
        model.addAttribute("error", e.getMessage());
        return "404";
    }
    
    @ExceptionHandler(DuplicateEntityException.class)
    public String EntityNotFoundExcHandler(DuplicateEntityException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "404";
    }
    
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleFileSizeException(MaxUploadSizeExceededException e, Model model) {
        model.addAttribute("error", "File has exceeded maximum upload size : " + maxFileUploadSize);
        return "404";
    }
    
}
