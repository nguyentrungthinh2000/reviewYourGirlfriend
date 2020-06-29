package com.rygf.dao;

import com.rygf.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);
    Optional<Role> findByName(String name);
}
