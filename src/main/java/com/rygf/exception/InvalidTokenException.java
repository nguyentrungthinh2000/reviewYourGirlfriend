package com.rygf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid Token")
public class InvalidTokenException extends RuntimeException {
    
    public InvalidTokenException() {
    }
    
    public InvalidTokenException(String message) {
        super(message);
    }
}
