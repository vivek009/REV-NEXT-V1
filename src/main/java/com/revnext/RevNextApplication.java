package com.revnext;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RevNextApplication {
    public static void main(String[] args) {
        SpringApplication.run(RevNextApplication.class, args);
    }
}

