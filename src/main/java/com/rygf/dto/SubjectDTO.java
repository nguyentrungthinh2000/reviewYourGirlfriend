package com.rygf.dto;

import com.rygf.entity.Image;
import com.rygf.entity.Thumbnail;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SubjectDTO {
    private Long id;
    
    private MultipartFile thumbnailFile;
    
    private Thumbnail thumbnail = new Thumbnail();
    private Image image = new Image();
    
    @NotBlank
    private String title;
    
    @NotBlank
    private String about;
    
}
