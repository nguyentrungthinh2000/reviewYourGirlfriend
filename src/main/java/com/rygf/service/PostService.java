package com.rygf.service;

import com.rygf.dao.PostRepository;
import com.rygf.dao.UserRepository;
import com.rygf.dto.PostDTO;
import com.rygf.entity.Post;
import com.rygf.entity.User;
import com.rygf.exception.DuplicateEntityException;
import com.rygf.exception.EntityNotFoundException;
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

@RequiredArgsConstructor
@Slf4j
//...
@Transactional
@Service
public class PostService implements IPostService {
    
    private final ServletContext servletContext;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    
    @Value("${post_thumb.upload.path}")
    private String uploadPath;
    
    @Value("${image.upload.maxSize}")
    private int uploadMaxSize;
    
    @Value("${post.page.size}")
    private int pageSize;
    
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
            temp.setSubject(postDTO.getSubject());
            if(postDTO.getFinalDesFileName() != null) {
                deleteExistThumbnail(postDTO.getId());
                temp.setThumbnail(postDTO.getFinalDesFileName());
            }
        } else { // CREATE NEW POST
            temp = new Post();
            temp.setTitle(postDTO.getTitle());
            temp.setDescription(postDTO.getDescription());
            temp.setContent(postDTO.getContent());
            temp.setAuthor(postDTO.getAuthor());
            temp.setSubject(postDTO.getSubject());
            temp.setThumbnail(postDTO.getFinalDesFileName());
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Optional<User> optUser = userRepository.findByEmail(auth.getName());
            optUser.orElseThrow(() -> new EntityNotFoundException("User with email : " + auth.getName() + " is not exists !"));
    
            temp.setAuthor(optUser.get());
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
        dto.setThumbnailUri(post.getThumbnail());
        dto.setSubject(post.getSubject());
        return dto;
    }
    
    public void deleteExistThumbnail(Long postId) {
        Optional<Post> opt = postRepository.findById(postId);
        opt.orElseThrow(() -> new EntityNotFoundException("Post with id : " + postId + " is not exists !"));
        
        Post post = opt.get();
        String filePath = servletContext.getRealPath("") + uploadPath.concat(post.getThumbnail());
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
