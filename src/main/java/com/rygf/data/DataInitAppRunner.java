package com.rygf.data;

import com.rygf.dto.RoleDTO;
import com.rygf.dto.UserDTO;
import com.rygf.entity.Role;
import com.rygf.security.permission.PRIVILEGE;
import com.rygf.service.RoleService;
import com.rygf.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
//..
@Component
public class DataInitAppRunner implements ApplicationRunner {
    
    private final UserService userService;
    private final RoleService roleService;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
    
        final String roleName = "ADMIN";
        Role revRole;
        if(!roleService.isRoleExists(roleName)) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setName(roleName);
            roleDTO.setPrivileges(Set.of(PRIVILEGE.values()));
            revRole = roleService.createOrUpdate(roleDTO);
        } else {
            revRole = roleService.findByName(roleName);
        }
    
        final String userEmail = "admin@test.com";
        if(!userService.isUserExists(userEmail)) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmail(userEmail);
            userDTO.setPassword("123");
            userDTO.setEnabled(true);
            userDTO.setRole(revRole);
            userService.createOrUpdate(userDTO);
        }
    }
}
