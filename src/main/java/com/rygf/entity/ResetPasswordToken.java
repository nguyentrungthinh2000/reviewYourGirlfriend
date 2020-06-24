package com.rygf.entity;

import java.time.LocalDate;
import javax.persistence.Entity;

@Entity
public class ResetPasswordToken extends VerificationToken {
    
    public ResetPasswordToken(String token, User user) {
        super(token, user, LocalDate.now().plusDays(3));
    }
    
    public ResetPasswordToken() {
    }
}
