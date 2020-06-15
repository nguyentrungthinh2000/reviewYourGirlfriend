package com.rygf.common;

import com.rygf.exception.ImageException;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class ImageUploader {
    
    @Autowired
    private ServletContext servletContext;
    
    @Value("${thumbnail.upload.path}")
    private String uploadPath;
    
    @Value("${image.upload.maxSize}")
    int uploadMaxSize;
    
    public String uploadFile(MultipartFile source) throws ImageException {
        imageFileValidCheck(source);
        
        File destinationFile;
        String destinationFileName;
        do {
            String sourceFileNameExtension = FilenameUtils.getExtension(source.getOriginalFilename()).toLowerCase();
            destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
            var desPath = servletContext.getRealPath("") + uploadPath.concat(destinationFileName);
            log.info("Uploaded Successfully with path : {}", desPath);
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
