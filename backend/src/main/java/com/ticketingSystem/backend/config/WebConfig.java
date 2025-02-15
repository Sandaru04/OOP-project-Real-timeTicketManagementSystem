package com.ticketingSystem.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig is a configuration class for setting up Cross-Origin Resource Sharing (CORS).
 * It allows the frontend (React app) to make requests to the backend, which is necessary during development
 * when the frontend and backend are hosted on different ports.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Adds CORS mappings to the application, enabling cross-origin requests from specific origins.
     * This method configures CORS settings for the entire application.
     *
     * @param registry the CORS registry used to define CORS mappings
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Enable CORS for all endpoints in the application
        registry.addMapping("/**")
                // Allow requests from the frontend React app running on localhost:3000
                .allowedOrigins("http://localhost:3000")
                // Allow specific HTTP methods (GET, POST, PUT, DELETE) for cross-origin requests
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                // Allow all headers in cross-origin requests
                .allowedHeaders("*");
    }
}
