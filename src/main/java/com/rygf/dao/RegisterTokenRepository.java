package com.rygf.dao;

import com.rygf.entity.RegisterToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisterTokenRepository extends JpaRepository<RegisterToken, Long> {
    Optional<RegisterToken> findByToken(String token);
}
