package com.rygf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Entity not found")
public class EntityNotFoundException extends RuntimeException {
    
    public EntityNotFoundException() {
    }
    
    public EntityNotFoundException(String message) {
        super(message);
    }
}
