package com.rygf.service;

import com.rygf.dto.SubjectDTO;
import com.rygf.entity.Subject;
import java.util.List;

public interface ISubjectService {
    
    void createOrUpdate(SubjectDTO subject);
    Subject find(Long id);
    List<Subject> findAll();
    void delete(Long id);
    
}
