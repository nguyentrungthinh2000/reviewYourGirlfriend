package com.rygf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories
@ServletComponentScan
public class ReviewYourGirlfriendApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ReviewYourGirlfriendApplication.class, args);
    }
    
}
