package com.pahappa.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main Spring Boot Application class for User Authentication System.
 * 
 * This class serves as the entry point for the Spring Boot application.
 * It extends SpringBootServletInitializer to support JSP deployment in embedded Tomcat.
 * 
 * @SpringBootApplication annotation enables:
 * - @Configuration: Marks this as a configuration class
 * - @EnableAutoConfiguration: Enables Spring Boot's auto-configuration
 * - @ComponentScan: Scans for components in com.pahappa.app package and sub-packages
 * 
 * @author Pahappa
 * @version 1.0
 */
@SpringBootApplication
public class UserAuthApplication extends SpringBootServletInitializer {

    /**
     * Main method - entry point for the application.
     * Launches the Spring Boot application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(UserAuthApplication.class, args);
    }

    /**
     * Configure the application for WAR deployment.
     * This method is required for JSP support in Spring Boot.
     * 
     * @param builder SpringApplicationBuilder instance
     * @return Configured SpringApplicationBuilder
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(UserAuthApplication.class);
    }
}

// Made with Bob
