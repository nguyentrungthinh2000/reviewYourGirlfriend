package com.rygf.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Slf4j
@Getter
//...
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final ImageRule imageRule = new ImageRule();
    
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ImageRule {
        private String bucketName;
        private String storageUri;
        private long maxUploadSize;
    }
    
}
