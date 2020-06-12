package com.rygf.exception.handler;

import com.rygf.exception.DuplicateEntityException;
import com.rygf.exception.EntityNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
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
    
}
