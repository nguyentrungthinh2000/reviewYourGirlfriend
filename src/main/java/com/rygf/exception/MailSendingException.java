package com.rygf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Mail sending was interrupted")
public class MailSendingException extends RuntimeException {
    
    public MailSendingException() {
    }
    
    public MailSendingException(String message) {
        super(message);
    }
}
