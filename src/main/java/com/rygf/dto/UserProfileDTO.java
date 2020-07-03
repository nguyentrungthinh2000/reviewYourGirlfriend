package com.rygf.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDTO {
    private String displayName;
    private MultipartFile thumbnail;
    private String embedThumbnailUri;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;
    private String bio;
    private String thumbnailUri;
    private String finalDesFileName;
}
