package com.ecosorter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * EcoSorter Backend Application
 * Main entry point for the Spring Boot application
 */
@SpringBootApplication
@EnableMongoAuditing
@EnableAsync
@EnableScheduling
public class EcoSorterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcoSorterApplication.class, args);
    }
}