package com.rygf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Settings are conflicted")
public class UserSettingException extends RuntimeException {
    
    public UserSettingException() {
    }
    
    public UserSettingException(String message) {
        super(message);
    }
}
