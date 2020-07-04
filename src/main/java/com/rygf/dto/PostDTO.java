package com.rygf.dto;

import com.rygf.entity.Subject;
import com.rygf.entity.Thumbnail;
import com.rygf.entity.User;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostDTO {
    private Long id;
    
    private MultipartFile thumbnailFile;
    
    private Thumbnail thumbnail = new Thumbnail();
    
    @NotBlank
    private String title;
    
    @NotBlank
    private String description;
    
    @NotBlank
    private String content;
    
    private User author;
    
    private Subject subject;
}
