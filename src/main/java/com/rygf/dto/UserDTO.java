package com.rygf.dto;

import com.rygf.entity.Role;
import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private Long id;
    
    @Email
    private String email;
    
    private String password;
    
    private boolean enabled;
    
    private Role role;
}
