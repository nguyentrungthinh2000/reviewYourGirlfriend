package com.rygf.dto;

import com.rygf.entity.Subject;
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
    
    private MultipartFile thumbnail;
    
    private String embedThumbnailUri;
    
    @NotBlank
    private String title;
    
    @NotBlank
    private String description;
    
    @NotBlank
    private String content;
    
    private User author;
    
    private String finalDesFileName;
    
    private String thumbnailUri;
    
    private Subject subject;
}
