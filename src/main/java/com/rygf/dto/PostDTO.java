package com.rygf.dto;

import com.rygf.entity.User;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostDTO {
    private Long id;
    
    @NotNull
    private MultipartFile thumbnail;
    
    @NotBlank
    @Length(min = 0, max = 30)
    private String title;
    
    @NotBlank
    private String description;
    
    @NotBlank
    private String content;
    
    private User author;
    
    private String finalDesFileName;
}
