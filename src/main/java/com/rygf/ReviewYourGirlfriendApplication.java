package com.rygf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@ServletComponentScan
public class ReviewYourGirlfriendApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ReviewYourGirlfriendApplication.class, args);
    }
    
}
