package com.rygf.dao;

import com.rygf.entity.ResetPasswordToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {
    Optional<ResetPasswordToken> findByToken(String token);
}
