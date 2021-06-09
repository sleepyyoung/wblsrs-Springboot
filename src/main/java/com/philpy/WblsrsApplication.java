package com.philpy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WblsrsApplication {
    public static void main(String[] args) {
        SpringApplication.run(WblsrsApplication.class, args);
    }

}
