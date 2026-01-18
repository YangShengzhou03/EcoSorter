package com.ecosorter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class EcoSorterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcoSorterApplication.class, args);
    }
}