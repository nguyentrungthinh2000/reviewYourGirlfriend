package com.rygf.controller;

import com.rygf.dto.CrudStatus;
import com.rygf.dto.CrudStatus.STATUS;
import com.rygf.dto.RoleDTO;
import com.rygf.entity.Role;
import com.rygf.security.permission.PRIVILEGE;
import com.rygf.service.RoleService;
import java.util.Arrays;
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
@RequestMapping("/dashboard/role")
@Controller
public class RoleController {
    
    private final RoleService roleService;
    
    @ModelAttribute("crudStatus")
    public CrudStatus getCrudStatus() {
        return new CrudStatus();
    }
    
    @ModelAttribute("privileges")
    public List<PRIVILEGE> getPrivileges() {
        return Arrays.asList(PRIVILEGE.values());
    }
    
    @PreAuthorize("hasAuthority('ROLE_READ')")
    @GetMapping
    public String showRoleDashboard(Model model) {
        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);
        
        return "role/dashboard";
    }
    
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    @GetMapping("/create")
    public String showRoleForm(@ModelAttribute("role") RoleDTO roleDTO) {
        return "role/form";
    }
    
    @PreAuthorize("hasAnyAuthority('ROLE_CREATE', 'ROLE_UPDATE')")
    @PostMapping("/submit")
    public String processForm(@Valid @ModelAttribute("role")RoleDTO roleDTO,
        BindingResult rs,
        RedirectAttributes ra
    ) {
        if(rs.hasErrors()) {
            return "role/form";
        }

        roleService.createOrUpdate(roleDTO);
        if(roleDTO.getId() == null)
            ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.CREATE_SUCCESS));
        else if(roleDTO.getId() != null)
            ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.UPDATE_SUCCESS));
        return "redirect:/dashboard/role";
    }
    
    @PreAuthorize("hasAuthority('ROLE_UPDATE')")
    @GetMapping("/{id}/update")
    public String showUpdatePage(@PathVariable("id")Long id,
            Model model
        ) {
        RoleDTO role = roleService.findDto(id);
        model.addAttribute("role", role);
        return "role/form";
    }
    
    @PreAuthorize("hasAuthority('ROLE_DELETE')")
    @GetMapping("/{id}/delete")
    public String processDelete(@PathVariable("id")Long id, RedirectAttributes ra) {
        roleService.delete(id);

        ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.DELETE_SUCCESS));
        return "redirect:/dashboard/role";
    }
    
}
