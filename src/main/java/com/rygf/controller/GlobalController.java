package com.rygf.controller;

import com.rygf.entity.Subject;
import com.rygf.service.SubjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalController {
    
    private final SubjectService subjectService;
    
    @ModelAttribute("subjects")
    public List<Subject> getSubjects() {
        return subjectService.findAllLimitTo(5).getContent();
    }
    
}
