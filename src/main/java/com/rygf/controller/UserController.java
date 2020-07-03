package com.rygf.controller;

import com.rygf.dto.CrudStatus;
import com.rygf.dto.CrudStatus.STATUS;
import com.rygf.dto.UserDTO;
import com.rygf.entity.Role;
import com.rygf.entity.User;
import com.rygf.service.RoleService;
import com.rygf.service.UserService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Slf4j
//...
@RequestMapping("/dashboard/users")
@Controller
public class UserController {
    
    private final UserService userService;
    private final RoleService roleService;
    
    @ModelAttribute("crudStatus")
    public CrudStatus getCrudStatus() {
        return new CrudStatus();
    }
    
    @ModelAttribute("roles")
    public List<Role> getRoles() {
        return roleService.findAll();
    }
    
    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping
    public String showUserDashboard(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        
        return "user/dashboard";
    }
    
    @PreAuthorize("hasAuthority('USER_CREATE')")
    @GetMapping("/create")
    public String showUserForm(@ModelAttribute("user") UserDTO userDTO) {
        return "user/form";
    }
    
    @PreAuthorize("hasAnyAuthority('USER_CREATE', 'USER_UPDATE')")
    @PostMapping("/submit")
    public String processForm(@Valid @ModelAttribute("user")UserDTO userDTO,
        BindingResult rs,
        RedirectAttributes ra
    ) {
        if(rs.hasErrors()) {
            return "user/form";
        }

        userService.createOrUpdate(userDTO);
        if(userDTO.getId() == null)
            ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.CREATE_SUCCESS));
        else if(userDTO.getId() != null)
            ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.UPDATE_SUCCESS));
        return "redirect:/dashboard/users";
    }
    
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @GetMapping("/{id}/update")
    public String showUpdatePage(@PathVariable("id")Long id,
            Model model
        ) {
        UserDTO user = userService.findDto(id);
        model.addAttribute("user", user);
        return "user/form";
    }
    
    @PreAuthorize("hasAuthority('USER_DELETE')")
    @GetMapping("/{id}/delete")
    public String processDelete(@PathVariable("id")Long id, RedirectAttributes ra) {
        userService.delete(id);

        ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.DELETE_SUCCESS));
        return "redirect:/dashboard/users";
    }
}
