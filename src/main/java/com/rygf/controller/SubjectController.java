package com.rygf.controller;

import com.rygf.common.ImageUploader;
import com.rygf.dto.CrudStatus;
import com.rygf.dto.CrudStatus.STATUS;
import com.rygf.dto.SubjectDTO;
import com.rygf.entity.Subject;
import com.rygf.exception.ImageException;
import com.rygf.service.SubjectService;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/dashboard/subject")
@Controller
@Slf4j
public class SubjectController {
    
    @Autowired
    private SubjectService subjectService;
    
    @Autowired
    private ImageUploader imageUploader;
    
    @ModelAttribute("crudStatus")
    public CrudStatus getCrudStatus() {
        return new CrudStatus();
    }
    
    @GetMapping
    public String showSubjectDashboard(Model model) {
        List<Subject> subjects = subjectService.findAll();
        model.addAttribute("subjects", subjects);
        
        return "subject/dashboard";
    }
    
    @GetMapping("/create")
    public String showSubjectForm(@ModelAttribute("subject") SubjectDTO subjectDTO) {
        return "subject/form";
    }

    @PostMapping("/submit")
    public String processForm(@Valid @ModelAttribute("subject")SubjectDTO subjectDTO,
        BindingResult rs,
        RedirectAttributes ra
    ) {
        if(rs.hasErrors()) {
            return "subject/form";
        }

        MultipartFile source = subjectDTO.getThumbnail();
        try {
            if(subjectDTO.getId() == null && (source == null || source.isEmpty()))
                throw new ImageException("ERR_UPLOAD_IMAGE_NULL");
            else if(subjectDTO.getId() != null && (source == null || source.isEmpty())) {
                // là trường hợp update nhưng không update Thumbnail
            } else {
                if(subjectDTO.getId() != null) // Xóa exists thumbnail
                    subjectService.deleteExistThumbnail(subjectDTO.getId());
                String finalDesFileName = imageUploader.uploadFile(source);
                subjectDTO.setFinalDesFileName(finalDesFileName);
            }

        } catch (ImageException e) {
            rs.rejectValue("thumbnail", null, e.getMessage());
            return "subject/form";
        }

        subjectService.createOrUpdate(subjectDTO);
        if(subjectDTO.getId() == null)
            ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.CREATE_SUCCESS));
        else if(subjectDTO.getId() != null)
            ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.UPDATE_SUCCESS));
        return "redirect:/dashboard/subject";
    }
    
    @GetMapping("/{id}/update")
    public String showUpdatePage(@PathVariable("id")Long id,
            Model model
        ) {
        SubjectDTO subject = subjectService.findDto(id);
        model.addAttribute("subject", subject);
        return "subject/form";
    }

    @GetMapping("/{id}/delete")
    public String processDelete(@PathVariable("id")Long id, RedirectAttributes ra) {
        subjectService.delete(id);

        ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.DELETE_SUCCESS));
        return "redirect:/dashboard/subject";
    }
    
}
