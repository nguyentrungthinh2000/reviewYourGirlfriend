package com.rygf.service;

import com.rygf.dao.PostRepository;
import com.rygf.dto.PostDTO;
import com.rygf.entity.Post;
import com.rygf.exception.DuplicateEntityException;
import com.rygf.exception.EntityNotFoundException;
import com.rygf.exception.ImageException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Transactional
@Service
public class PostService implements IPostService {
    
    @Autowired
    private ServletContext servletContext;
    
    @Value("${thumbnail.upload.path}")
    private String uploadPath;
    
    @Value("${image.upload.maxSize}")
    int uploadMaxSize;
    
    @Autowired
    private PostRepository postRepository;
    
    @Override
    public void createOrUpdate(PostDTO postDTO) {
        Post temp;
        
        if(postDTO.getId() != null) { // UPDATE POST
            Optional<Post> opt = postRepository.findById(postDTO.getId());
            final var postId = postDTO.getId();
            opt.orElseThrow(() -> new EntityNotFoundException("Post with id : " + postId + " is not exists !"));
    
            temp = opt.get();
            temp.setTitle(postDTO.getTitle());
            temp.setDescription(postDTO.getDescription());
            temp.setContent(postDTO.getContent());
            temp.setAuthor(postDTO.getAuthor());
            temp.setThumbnail(postDTO.getFinalDesFileName());
        } else { // CREATE NEW POST
            temp = new Post();
            temp.setTitle(postDTO.getTitle());
            temp.setDescription(postDTO.getDescription());
            temp.setContent(postDTO.getContent());
            temp.setAuthor(postDTO.getAuthor());
            temp.setThumbnail(postDTO.getFinalDesFileName());
        }
        
        try {
            postRepository.save(temp);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new DuplicateEntityException("Duplicated Post\n" + e.getMessage());
        }
    }
    
    @Override
    public Post find(Long id) {
        Optional<Post> opt = postRepository.findById(id);
        opt.orElseThrow(() -> new EntityNotFoundException("Post with id : " + id + " is not exists !"));
    
        return opt.get();
    }
    
    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }
    
    @Override
    public void delete(Long id) {
        Optional<Post> opt = postRepository.findById(id);
        opt.orElseThrow(() -> new EntityNotFoundException("Post with id : " + id + " is not exists !"));
    
        postRepository.delete(opt.get());
    }
    
    /*
    *  NOT CRUD
    * */
    public String uploadFile(MultipartFile source) throws ImageException {
        imageFileValidCheck(source);
    
        File destinationFile;
        String destinationFileName;
        do {
            String sourceFileNameExtension = FilenameUtils.getExtension(source.getOriginalFilename()).toLowerCase();
            destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
            var desPath = servletContext.getRealPath("") + uploadPath.concat(destinationFileName);
            log.info("path : {}", desPath);
            destinationFile = new File(desPath);
        } while (destinationFile.exists());
        destinationFile.getParentFile().mkdirs();
    
        try {
            source.transferTo(destinationFile);
        } catch (IOException e) {
            log.warn("File can't be upload {}", e.getMessage());
        }
    
        return destinationFileName;
    }
    
    private boolean imageFileValidCheck(MultipartFile source) throws ImageException {
        // file null check
        if (source == null || source.isEmpty())
            throw new ImageException("ERR_UPLOAD_IMAGE_NULL");
        
        // file extension check
        Pattern p = Pattern.compile("\\.(jpg|jpeg|png|JPG|JPEG|PNG)$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(source.getOriginalFilename());
        if (!m.find())
            throw new ImageException("ERR_UPLOAD_IMAGE_EXT");
        
        // file size check (5Mb under..)
        if (source.getSize() > uploadMaxSize)
            throw new ImageException("ERR_UPLOAD_IMAGE_SIZE");
        
        return true;
    }
}
