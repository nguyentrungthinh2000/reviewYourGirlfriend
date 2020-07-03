package com.rygf.service;

import com.rygf.common.ImageUploader;
import com.rygf.dao.PostRepository;
import com.rygf.dao.UserRepository;
import com.rygf.dto.PostDTO;
import com.rygf.entity.Post;
import com.rygf.entity.User;
import com.rygf.exception.DuplicateEntityException;
import com.rygf.exception.EntityNotFoundException;
import com.rygf.exception.ImageException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Slf4j
//...
@Transactional
@Service
public class PostService implements IPostService {
    
    private final ServletContext servletContext;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageUploader imageUploader;
    
    @Value("${post_thumb.upload.path}")
    private String uploadPath;
    
    @Value("${post.page.size}")
    private int pageSize;
    
    @Override
    public void createOrUpdate(PostDTO dto) {
        Post temp;
        
        if(dto.getId() != null) { // UPDATE POST
            Optional<Post> opt = postRepository.findById(dto.getId());
            final var postId = dto.getId();
            opt.orElseThrow(() -> new EntityNotFoundException("Post with id : " + postId + " is not exists !"));
    
            temp = opt.get();
            temp.setTitle(dto.getTitle());
            temp.setDescription(dto.getDescription());
            temp.setContent(dto.getContent());
            temp.setSubject(dto.getSubject());
        } else { // CREATE NEW POST
            temp = new Post();
            temp.setTitle(dto.getTitle());
            temp.setDescription(dto.getDescription());
            temp.setContent(dto.getContent());
            temp.setAuthor(dto.getAuthor());
            temp.setSubject(dto.getSubject());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Optional<User> optUser = userRepository.findByEmail(auth.getName());
            optUser.orElseThrow(() -> new EntityNotFoundException("User with email : " + auth.getName() + " is not exists !"));
    
            temp.setAuthor(optUser.get());
        }
        
        // Thumbnail
        deleteExistThumbnail(dto.getId());
        if(dto.getFinalDesFileName() != null) {
            //Thumbnail
            temp.getThumbnail().setUri(dto.getFinalDesFileName());
            temp.getThumbnail().setEmbedded(false); // not embed link
        } else if(dto.getFinalDesFileName() == null && dto.getEmbedThumbnailUri() != null) {
            temp.getThumbnail().setUri(dto.getEmbedThumbnailUri());
            temp.getThumbnail().setEmbedded(true); // embed link
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
    
    public List<Post> findByUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        userOpt.orElseThrow(() -> new EntityNotFoundException("User with id : " + id + " is not exists !"));
        return postRepository.findByUser(id);
    }
    
    @Override
    public List<Post> findAll() {
        ArrayList<Post> posts = new ArrayList<>();
        postRepository.findAll().forEach(post -> {
            posts.add(post);
        });
        
        return posts;
    }
    
    @Override
    public void delete(Long id) {
        Optional<Post> opt = postRepository.findById(id);
        opt.orElseThrow(() -> new EntityNotFoundException("Post with id : " + id + " is not exists !"));
    
        Post post = opt.get();
        deleteExistThumbnail(id);
        postRepository.delete(post);
    }
    
    /*
    *  NOT CRUD
    * */
    public PostDTO findDto(Long id) {
        Optional<Post> opt = postRepository.findById(id);
        opt.orElseThrow(() -> new EntityNotFoundException("Post with id : " + id + " is not exists !"));
        PostDTO dto = new PostDTO();
        Post post = opt.get();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setContent(post.getContent());
        dto.setThumbnailUri(post.selfLinkThumbUri());
        dto.setSubject(post.getSubject());
        return dto;
    }
    
    public void uploadFile(PostDTO postDTO, MultipartFile source) throws ImageException {
        if(postDTO.getId() == null && (source == null || source.isEmpty()))
            throw new ImageException("ERR_UPLOAD_IMAGE_NULL");
        else if(postDTO.getId() != null && (source == null || source.isEmpty())) {
            // là trường hợp update nhưng không update Thumbnail
        } else {
            if(postDTO.getId() != null) // Xóa exists thumbnail
                deleteExistThumbnail(postDTO.getId());
            String finalDesFileName = imageUploader.uploadFile(source, uploadPath);
            postDTO.setFinalDesFileName(finalDesFileName);
        }
    }
    
    public void deleteExistThumbnail(Long postId) {
        Optional<Post> opt = postRepository.findById(postId);
        opt.orElseThrow(() -> new EntityNotFoundException("Post with id : " + postId + " is not exists !"));
        
        Post post = opt.get();
        final String thumbUri = post.getThumbnail().getUri();
        if(thumbUri == null || thumbUri.isBlank())
            return;
        
        if(post.getThumbnail().isEmbedded()) // Sử dụng Embedded --> không phải xóa
            return;
        String filePath = servletContext.getRealPath("") + uploadPath.concat(post.getThumbnail().getUri());
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch(IOException e) {
            log.warn("IOException : {}", e.getMessage());
        }
    }
    
    public Page<Post> findAllPaginated(int curPage, String orderBy, String orderDir) {
        Sort orders;
        Pageable pageable;
        pageable = PageRequest.of(curPage - 1, pageSize);
        if(orderBy != null) {
            orders = Sort.by(orderBy);
            if(orderDir != null) {
                switch (orderDir) {
                    case "asc":
                        orders = orders.ascending();
                        break;
                    case "desc":
                        orders = orders.descending();
                        break;
                    default:
                        orders = orders.ascending();
                }
            }
            pageable = PageRequest.of(curPage, pageSize, orders);
        }
        return postRepository.findAll(pageable);
    }
}
