package com.rygf.dto;

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
    
    private MultipartFile thumbnail;
    
    @NotBlank
    private String title;
    
    @NotBlank
    private String about;
    
    private String finalDesFileName;
    
    private String thumbnailUri;
    
}
