package com.rygf.service;

import com.rygf.dao.SubjectRepository;
import com.rygf.dto.SubjectDTO;
import com.rygf.entity.Subject;
import com.rygf.exception.DuplicateEntityException;
import com.rygf.exception.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
public class SubjectService implements ISubjectService {
    
    @Autowired
    private ServletContext servletContext;
    
    @Value("${thumbnail.upload.path}")
    private String uploadPath;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Override
    public void createOrUpdate(SubjectDTO subjectDTO) {
        Subject temp;

        if(subjectDTO.getId() != null) { // UPDATE SUBJECT
            Optional<Subject> opt = subjectRepository.findById(subjectDTO.getId());
            final var subjectId = subjectDTO.getId();
            opt.orElseThrow(() -> new EntityNotFoundException("Subject with id : " + subjectId + " is not exists !"));

            temp = opt.get();
            temp.setTitle(subjectDTO.getTitle());
            temp.setAbout(subjectDTO.getAbout());
            if(subjectDTO.getFinalDesFileName() != null) {
                deleteExistThumbnail(subjectDTO.getId());
                temp.setThumbnail(subjectDTO.getFinalDesFileName());
            }
        } else { // CREATE NEW SUBJECT
            temp = new Subject();
            temp.setTitle(subjectDTO.getTitle());
            temp.setAbout(subjectDTO.getAbout());
            temp.setThumbnail(subjectDTO.getFinalDesFileName());
        }

        try {
            subjectRepository.save(temp);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new DuplicateEntityException("Duplicated Subject\n" + e.getMessage());
        }
    }
    
    @Override
    public Subject find(Long id) {
        Optional<Subject> opt = subjectRepository.findById(id);
        opt.orElseThrow(() -> new EntityNotFoundException("Subject with id : " + id + " is not exists !"));
    
        Subject subject = opt.get();
        subject.getPosts().size(); //invoke lazy load
        
        return subject;
    }
    
    @Override
    public List<Subject> findAll() {
        ArrayList<Subject> subjects = new ArrayList<>();
        subjectRepository.findAll().forEach(subject -> {
            subjects.add(subject);
        });

        return subjects;
    }

    @Override
    public void delete(Long id) {
        Optional<Subject> opt = subjectRepository.findById(id);
        opt.orElseThrow(() -> new EntityNotFoundException("Subject with id : " + id + " is not exists !"));

        Subject subject = opt.get();
        deleteExistThumbnail(id);
        subjectRepository.delete(subject);
    }
    
    /*
    *  NOT CRUD
    * */
    
    public SubjectDTO findDto(Long id) {
        Optional<Subject> opt = subjectRepository.findById(id);
        opt.orElseThrow(() -> new EntityNotFoundException("Subject with id : " + id + " is not exists !"));
        SubjectDTO temp = new SubjectDTO();
        Subject subject = opt.get();
        temp.setId(subject.getId());
        temp.setTitle(subject.getTitle());
        temp.setAbout(subject.getAbout());
        temp.setThumbnailUri(subject.getThumbnail());
        return temp;
    }
    
    public void deleteExistThumbnail(Long subjectId) {
        Optional<Subject> opt = subjectRepository.findById(subjectId);
        opt.orElseThrow(() -> new EntityNotFoundException("Subject with id : " + subjectId + " is not exists !"));

        Subject subject = opt.get();
        String filePath = servletContext.getRealPath("") + uploadPath.concat(subject.getThumbnail());
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch(IOException e) {
            log.warn("IOException : {}", e.getMessage());
        }
    }
    
    public Page<Subject> findAllLimitTo(int limit) {
        PageRequest pageable = PageRequest.of(0, limit);
        return subjectRepository.findAll(pageable);
    }
    

//    public Page<Subject> findAllPaginated(int curPage, String orderBy, String orderDir) {
//        Sort orders;
//        Pageable pageable;
//        pageable = PageRequest.of(curPage - 1, pageSize);
//        if(orderBy != null) {
//            orders = Sort.by(orderBy);
//            if(orderDir != null) {
//                switch (orderDir) {
//                    case "asc":
//                        orders = orders.ascending();
//                        break;
//                    case "desc":
//                        orders = orders.descending();
//                        break;
//                    default:
//                        orders = orders.ascending();
//                }
//            }
//            pageable = PageRequest.of(curPage, pageSize, orders);
//        }
//        return subjectRepository.findAll(pageable);
//    }
}
