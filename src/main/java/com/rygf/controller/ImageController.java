package com.rygf.controller;

import com.rygf.entity.Image;
import com.rygf.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/images")
@RestController
public class ImageController {
    
    @Autowired
    private ImageService imageService;
    
//    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/url")
    public Image uploadImageFromUrl(@RequestParam("image_url") String imageUrl) {
        return imageService.uploadFileFromUrl(imageUrl);
    }
    
//    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Image uploadImage(@RequestParam("file") MultipartFile multipartFile) {
        return imageService.uploadFile(multipartFile);
    }
    
    
    // ADMIN | ACL
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void deleteImage(@RequestParam("filename") String filename) {
        imageService.deleteImage(filename);
    }
    
}
