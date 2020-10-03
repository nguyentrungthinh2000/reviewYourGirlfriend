package com.rygf.image;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.rygf.config.AppProperties;
import com.rygf.config.AppProperties.ImageRule;
import com.rygf.entity.Image;
import com.rygf.exception.ImageException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
//..
@Service
public class ImageService {
    private final Storage storage;
    private final AppProperties appProperties;
    private ImageRule imageRule;
    
    @PostConstruct
    private void postConstruct() {
        this.imageRule = appProperties.getImageRule();
    }
    
    
    public Image uploadFileFromUrl(String imageUrl) throws ImageException {
        try (InputStream in = new URL(imageUrl).openStream()) {
            URL url = new URL(imageUrl);
            // mime
            URLConnection connection = url.openConnection();
            String mimeType = connection.getContentType();
    
            String fileExtension = "no_extension";
            try {
                MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
                MimeType type = allTypes.forName(mimeType);
                fileExtension = type.getExtension(); // .jpg
            } catch (MimeTypeException e) {
                e.printStackTrace();
            }
            
            // filename
            String destinationFileName =
                RandomStringUtils.randomAlphanumeric(32) + fileExtension;
    
            byte[] fileBytes = in.readAllBytes();
            imageFileValidCheck(destinationFileName, fileBytes.length);
            
            BlobId id = BlobId.of(imageRule.getBucketName(), destinationFileName);
            BlobInfo info = BlobInfo.newBuilder(id)
                .setContentType(mimeType)
                .build();
            
            Blob blob = storage.create(info, fileBytes);
            return new Image(destinationFileName,
                String.format("%s/%s/%s", imageRule.getStorageUri(),
                    imageRule.getBucketName(),
                    destinationFileName));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Image uploadFile(MultipartFile source) throws ImageException{
        if(source == null || source.isEmpty())
            throw new ImageException("Can't upload image : no image found");
        
        // filename
        String extension = FilenameUtils
            .getExtension(source.getOriginalFilename()).toLowerCase();
        String destinationFileName =
            RandomStringUtils.randomAlphanumeric(32) + "." + extension;
    
        imageFileValidCheck(destinationFileName, Long.valueOf(source.getSize()).intValue());
        
        BlobId blobId = BlobId.of(imageRule.getBucketName(), destinationFileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType(source.getContentType())
            .build();
        try {
            Blob blob = storage.create(blobInfo, source.getBytes());
            return new Image(destinationFileName,
                String.format("%s/%s/%s", imageRule.getStorageUri(),
                    imageRule.getBucketName(),
                    destinationFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void deleteImage(String filename) {
        try {
            Blob blob = storage.get(BlobId.of(imageRule.getBucketName(), filename));
            blob.delete();
        } catch (NullPointerException e) {
            throw new ImageException(
                "No Image was found to be delete with filename : " + filename);
        }
    }
    
    private boolean imageFileValidCheck(String filename, int size) throws ImageException {
        // file extension check
        Pattern p = Pattern.compile("\\.(jpg|jpeg|png|JPG|JPEG|PNG)$",
            Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(filename);
        if (!m.find()) {
            throw new ImageException("Only support jpg, jpeg, png (image file)");
        }
    
        // file size check (5Mb under..)
        if (size > imageRule.getMaxUploadSize()) {
            throw new ImageException(
                String.format("Exceed max upload size (maximum : %s) with your file: %s",
                    imageRule.getMaxUploadSize(),
                    size));
        }
        
        return true;
    }
}
