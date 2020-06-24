package com.rygf.dao;

import com.rygf.entity.ChangePasswordToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChangePasswordTokenRepository extends JpaRepository<ChangePasswordToken, Long> {
    Optional<ChangePasswordToken> findByToken(String token);
}
