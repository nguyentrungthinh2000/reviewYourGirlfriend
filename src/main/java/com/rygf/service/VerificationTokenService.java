package com.rygf.service;

import com.rygf.dao.ResetPasswordTokenRepository;
import com.rygf.dao.RegisterTokenRepository;
import com.rygf.entity.VerificationToken;
import com.rygf.entity.ResetPasswordToken;
import com.rygf.entity.RegisterToken;
import com.rygf.exception.InvalidTokenException;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
//...
@Service
@Transactional
public class VerificationTokenService {
    
    private final RegisterTokenRepository registerRepository;
    private final ResetPasswordTokenRepository changePasswordRepository;
    private final EntityManager em;
    
    public void saveToken(VerificationToken token) {
        em.persist(token);
    }
    
    public RegisterToken findRegisterTokenByToken(String token) {
        Optional<RegisterToken> opt = registerRepository.findByToken(token);
        opt.orElseThrow(() -> new InvalidTokenException("Invalid Token"));
        
        return opt.get();
    }
    
    public ResetPasswordToken findResetPasswordTokenByToken(String token) {
        Optional<ResetPasswordToken> opt = changePasswordRepository.findByToken(token);
        opt.orElseThrow(() -> new InvalidTokenException("Invalid Token"));
        
        return opt.get();
    }
    
}
