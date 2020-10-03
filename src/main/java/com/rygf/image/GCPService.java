package com.rygf.image;


import com.google.cloud.Identity;
import com.google.cloud.Policy;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageRoles;
import com.rygf.config.AppProperties;
import com.rygf.config.AppProperties.ImageRule;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GCPService implements ApplicationRunner {
    private final Storage storage;
    private final AppProperties appProperties;
    private ImageRule imageRule;
    
    @PostConstruct
    private void postConstruct() {
        this.imageRule = appProperties.getImageRule();
    }
    
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        Policy iamPolicy = storage.getIamPolicy(imageRule.getBucketName());
        storage.setIamPolicy(imageRule.getBucketName(),
            iamPolicy.toBuilder()
                .addIdentity(StorageRoles.objectViewer(), Identity.allUsers())
                .build());
    }
}
