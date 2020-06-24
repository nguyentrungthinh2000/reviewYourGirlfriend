package com.rygf.entity;

import java.time.LocalDate;
import javax.persistence.Entity;

@Entity
public class RegisterToken extends VerificationToken {
    
    public RegisterToken(String token, User user) {
        super(token, user, LocalDate.now().plusDays(3));
    }
    
    public RegisterToken() {
    }
}
