package com.rygf.security;

import com.rygf.dao.UserRepository;
import com.rygf.entity.Role;
import com.rygf.entity.User;
import com.rygf.exception.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> opt = userRepository.findByEmail(email);
        opt.orElseThrow(() -> new EntityNotFoundException("Email or password can be wrong !"));
    
        Role role = opt.get().getRole();
        if(role != null)
            role.getPrivileges().size(); // fetch lazy privileges
        CustomUserDetails userDetails = new CustomUserDetails(opt.get());
    
        return userDetails;
    }
}
