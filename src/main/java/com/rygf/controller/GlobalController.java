package com.rygf.controller;

import com.rygf.entity.Subject;
import com.rygf.service.SubjectService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@AllArgsConstructor
@ControllerAdvice
public class GlobalController {
    
    private SubjectService subjectService;
    
    @ModelAttribute("subjects")
    public List<Subject> getSubjects() {
        return subjectService.findAllLimitTo(5).getContent();
    }
    
}
