package com.example.lb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LbApplication {

    public static void main(String[] args) {
        SpringApplication.run(LbApplication.class, args);
    }

}
