package com.rygf.security;

import com.rygf.entity.Role;
import com.rygf.entity.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    
    private User user;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        Role role = user.getRole();
        if(role == null)
            return authorities;
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        
        if(role.getPrivileges().size() == 0)
            return authorities;
        List<SimpleGrantedAuthority> privileges = role.getPrivileges().stream()
            .map(privilege -> new SimpleGrantedAuthority(privilege.name())).collect(Collectors.toList());
        authorities.addAll(privileges);
        
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    
    @Override
    public String getUsername() {
        return user.getEmail();
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
