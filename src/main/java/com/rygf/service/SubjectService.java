package com.rygf.service;

import com.rygf.common.ImageUploader;
import com.rygf.dao.SubjectRepository;
import com.rygf.dto.SubjectDTO;
import com.rygf.entity.Subject;
import com.rygf.exception.DuplicateEntityException;
import com.rygf.exception.EntityNotFoundException;
import com.rygf.image.ImageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
//..
@Transactional
@Service
public class SubjectService implements ISubjectService {
    
    private final SubjectRepository subjectRepository;
    private final ServletContext servletContext;
    private final ImageUploader imageUploader;
    private final ImageService imageService;
    
    @Value("${post_thumb.upload.path}")
    private String uploadPath;
    
    
    @Override
    public void createOrUpdate(SubjectDTO dto) {
        Subject temp;

        if(dto.getId() != null) { // UPDATE SUBJECT
            Optional<Subject> opt = subjectRepository.findById(dto.getId());
            final var subjectId = dto.getId();
            opt.orElseThrow(() -> new EntityNotFoundException("Subject with id : " + subjectId + " is not exists !"));

            temp = opt.get();
            temp.setTitle(dto.getTitle());
            temp.setAbout(dto.getAbout());
            deleteExistThumbnail(dto.getId()); // Chỉ khi nào update --> mới delete
        } else { // CREATE NEW SUBJECT
            temp = new Subject();
            temp.setTitle(dto.getTitle());
            temp.setAbout(dto.getAbout());
        }
        
        // Thumbnail
        if(dto.getImage() != null) {
            temp.setImage(dto.getImage());
        }
    
        log.info("Subject name : {}", temp.getTitle());
        log.info("Subject about : {}", temp.getAbout());
        log.info("Subject image : {}", temp.getImage());
        
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
        SubjectDTO dto = new SubjectDTO();
        Subject subject = opt.get();
        dto.setId(subject.getId());
        dto.setTitle(subject.getTitle());
        dto.setAbout(subject.getAbout());
    
        /*
         *   Chú ý Transaction, Dirty check nhé
         * */
//        dto.getThumbnail().setUri(subject.selfLinkThumbUri());
//        dto.getThumbnail().setEmbedded(subject.getThumbnail().isEmbedded());
        dto.setImage(subject.getImage());
        
        
        return dto;
    }
    
    public void deleteExistThumbnail(Long subjectId) {
        Optional<Subject> opt = subjectRepository.findById(subjectId);
        opt.orElseThrow(() -> new EntityNotFoundException("Subject with id : " + subjectId + " is not exists !"));
        
        Subject subject = opt.get();
        final String filename = subject.getImage().getFilename();
        if(filename == null || filename.isBlank())
            return;
        
        imageService.deleteImage(filename);
    }
    
    public Page<Subject> findAllLimitTo(int limit) {
        PageRequest pageable = PageRequest.of(0, limit);
        return subjectRepository.findAll(pageable);
    }
    
}
