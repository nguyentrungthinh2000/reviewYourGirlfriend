package com.rygf.controller;

import static com.rygf.common.ViewName.SUBJECT_DASHBOARD_VIEW;
import static com.rygf.common.ViewName.SUBJECT_FORM_VIEW;

import com.rygf.dto.CrudStatus;
import com.rygf.dto.CrudStatus.STATUS;
import com.rygf.dto.SubjectDTO;
import com.rygf.entity.Image;
import com.rygf.entity.Subject;
import com.rygf.exception.ImageException;
import com.rygf.image.ImageService;
import com.rygf.service.SubjectService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Slf4j
//...
@RequestMapping("/dashboard/subjects")
@Controller
public class SubjectController {
    
    private final SubjectService subjectService;
    private final ImageService imageService;
    
    @ModelAttribute("crudStatus")
    public CrudStatus getCrudStatus() {
        return new CrudStatus();
    }
    
    @PreAuthorize("hasAuthority('SUBJECT_READ')")
    @GetMapping
    public String showSubjectDashboard(Model model) {
        List<Subject> subjects = subjectService.findAll();
        model.addAttribute("subjects", subjects);
        
        return SUBJECT_DASHBOARD_VIEW;
    }
    
    @PreAuthorize("hasAuthority('SUBJECT_CREATE')")
    @GetMapping("/create")
    public String showSubjectForm(@ModelAttribute("subject") SubjectDTO subjectDTO) {
        return SUBJECT_FORM_VIEW;
    }
    
//    @PreAuthorize("hasAnyAuthority('SUBJECT_CREATE', 'SUBJECT_UPDATE')")
//    @PostMapping("/submit")
//    public String processForm(@Valid @ModelAttribute("subject")SubjectDTO subjectDTO,
//        BindingResult rs,
//        RedirectAttributes ra
//    ) {
//        if(rs.hasErrors()) {
//            return SUBJECT_FORM_VIEW;
//        }
//
//        MultipartFile source = subjectDTO.getThumbnailFile();
//        if(!subjectDTO.getThumbnail().isEmbedded()) {
//            try {
//                subjectService.uploadFile(subjectDTO, source);
//            } catch (ImageException e) {
//                rs.rejectValue("thumbnail", null, e.getMessage());
//                return SUBJECT_FORM_VIEW;
//            }
//        } else {
//            subjectDTO.setThumbnailFile(null); // only use URI THUMBNAIL
//        }
//
//        subjectService.createOrUpdate(subjectDTO);
//        if(subjectDTO.getId() == null)
//            ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.CREATE_SUCCESS));
//        else if(subjectDTO.getId() != null)
//            ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.UPDATE_SUCCESS));
//        return "redirect:/dashboard/subjects";
//    }
    
    @PreAuthorize("hasAnyAuthority('SUBJECT_CREATE', 'SUBJECT_UPDATE')")
    @PostMapping("/submit")
    public String processForm(@Valid @ModelAttribute("subject")SubjectDTO subjectDTO,
        BindingResult rs,
        RedirectAttributes ra
    ) {
        if(rs.hasErrors()) {
            return SUBJECT_FORM_VIEW;
        }
        
        MultipartFile source = subjectDTO.getThumbnailFile();
        Image uploadedImage = null;
        try {
            if(!subjectDTO.getThumbnail().isEmbedded()) {
                uploadedImage = imageService.uploadFile(source);
            } else {
                uploadedImage = imageService.uploadFileFromUrl(subjectDTO.getThumbnail().getUri());
            }
        } catch (ImageException e) {
            rs.rejectValue("thumbnail", null, e.getMessage());
            return SUBJECT_FORM_VIEW;
        }
    
        log.info("Upload Image : {}", uploadedImage);
        subjectDTO.setImage(uploadedImage);
        subjectService.createOrUpdate(subjectDTO);
        if(subjectDTO.getId() == null)
            ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.CREATE_SUCCESS));
        else if(subjectDTO.getId() != null)
            ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.UPDATE_SUCCESS));
        return "redirect:/dashboard/subjects";
    }
    
    @PreAuthorize("hasAuthority('SUBJECT_UPDATE')")
    @GetMapping("/{id}/update")
    public String showUpdatePage(@PathVariable("id")Long id,
            Model model
        ) {
        SubjectDTO subject = subjectService.findDto(id);
        model.addAttribute("subject", subject);
        return SUBJECT_FORM_VIEW;
    }
    
    @PreAuthorize("hasAuthority('SUBJECT_DELETE')")
    @GetMapping("/{id}/delete")
    public String processDelete(@PathVariable("id")Long id, RedirectAttributes ra) {
        subjectService.delete(id);

        ra.addFlashAttribute("crudStatus", new CrudStatus(STATUS.DELETE_SUCCESS));
        return "redirect:/dashboard/subjects";
    }
    
}
