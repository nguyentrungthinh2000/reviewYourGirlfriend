package com.rygf;

import com.rygf.common.Formatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories
@ServletComponentScan
public class ReviewYourGirlfriendApplication implements CommandLineRunner {
    
    public static void main(String[] args) {
        SpringApplication.run(ReviewYourGirlfriendApplication.class, args);
    }
    
    @Override
    public void run(String... args) throws Exception {
    }
}
