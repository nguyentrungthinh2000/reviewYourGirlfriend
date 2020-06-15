package com.rygf.service;

import com.rygf.dao.PostRepository;
import com.rygf.dto.PostDTO;
import com.rygf.entity.Post;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    @Value("${post.page.size}")
    private int pageSize;
    
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
            temp.setSubject(postDTO.getSubject());
            if(postDTO.getFinalDesFileName() != null)
                temp.setThumbnail(postDTO.getFinalDesFileName());
        } else { // CREATE NEW POST
            temp = new Post();
            temp.setTitle(postDTO.getTitle());
            temp.setDescription(postDTO.getDescription());
            temp.setContent(postDTO.getContent());
            temp.setAuthor(postDTO.getAuthor());
            temp.setSubject(postDTO.getSubject());
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
        PostDTO temp = new PostDTO();
        Post post = opt.get();
        temp.setId(post.getId());
        temp.setTitle(post.getTitle());
        temp.setDescription(post.getDescription());
        temp.setContent(post.getContent());
        temp.setThumbnailUri(post.getThumbnail());
        temp.setSubject(post.getSubject());
        return temp;
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
