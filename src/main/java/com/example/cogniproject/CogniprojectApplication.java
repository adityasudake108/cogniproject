package com.example.cogniproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Entry point for the HealthNet Disease Case Reporting & Tracking System (Module 3).
 */
@SpringBootApplication
@EnableJpaAuditing
public class CogniprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(CogniprojectApplication.class, args);
    }
}
