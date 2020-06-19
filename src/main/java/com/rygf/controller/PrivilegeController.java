package com.rygf.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@AllArgsConstructor
@Slf4j
//...
@RequestMapping("/dashboard/privilege")
@Controller
public class PrivilegeController {
    
//    private PrivilegeService privilegeService;
//
//    @ModelAttribute("crudStatus")
//    public CrudStatus getCrudStatus() {
//        return new CrudStatus();
//    }
//
//    @GetMapping
//    public String showPrivilegeDashboard(Model model) {
//        List<Privilege> privileges = privilegeService.findAll();
//        model.addAttribute("privileges", privileges);
//
//        return "privilege/dashboard";
//    }
//
//    @GetMapping("/create")
//    public String showPrivilegeForm(@ModelAttribute("privilege") PrivilegeDTO privilegeDTO) {
//        return "privilege/form";
//    }
//
//    @PostMapping("/submit")
//    public String processForm(@Valid @ModelAttribute("privilege")PrivilegeDTO privilegeDTO,
//        BindingResult rs,
//        RedirectAttributes ra
//    ) {
//        if(rs.hasErrors()) {
//            return "privilege/form";
//        }
//
//        privilegeService.createOrUpdate(privilegeDTO);
//        if(privilegeDTO.getId() == null)
//            ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.CREATE_SUCCESS));
//        else if(privilegeDTO.getId() != null)
//            ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.UPDATE_SUCCESS));
//        return "redirect:/dashboard/privilege";
//    }
//
//    @GetMapping("/{id}/update")
//    public String showUpdatePage(@PathVariable("id")Long id,
//            Model model
//        ) {
//        PrivilegeDTO privilege = privilegeService.findDto(id);
//        model.addAttribute("privilege", privilege);
//        return "privilege/form";
//    }
//
//    @GetMapping("/{id}/delete")
//    public String processDelete(@PathVariable("id")Long id, RedirectAttributes ra) {
//        privilegeService.delete(id);
//
//        ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.DELETE_SUCCESS));
//        return "redirect:/dashboard/privilege";
//    }

}
