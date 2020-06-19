package com.rygf.dto;

import com.rygf.security.permission.PRIVILEGE;
import java.util.Collection;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoleDTO {
    private Long id;
    
    @NotBlank
    private String name;
    
    private Collection<PRIVILEGE> privileges;
}
