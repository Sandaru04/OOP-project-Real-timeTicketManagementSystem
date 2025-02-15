package com.ticketingSystem.backend.polling;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for handling Cross-Origin Resource Sharing (CORS) in the ticketing system application.
 * This allows the front-end application running on a different domain (e.g., localhost:3000) to access the API.
 */
@Configuration
public class PollingConfig implements WebMvcConfigurer {

    /**
     * Configures CORS mappings to allow requests from specific origins.
     * This is particularly useful for enabling communication between the front-end and back-end
     * during development when they run on different ports (e.g., front-end on localhost:3000 and back-end on localhost:8080).
     *
     * @param registry the CorsRegistry to configure allowed CORS mappings
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Allow requests from "http://localhost:3000" to access any endpoint on the back-end
        registry.addMapping("/**").allowedOrigins("http://localhost:3000");
    }
}
