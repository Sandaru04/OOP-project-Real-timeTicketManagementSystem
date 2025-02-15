package com.ticketingSystem.backend.controller;

import com.ticketingSystem.backend.model.ConfigurationEntity;
import com.ticketingSystem.backend.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The ConfigurationController class provides REST API endpoints for managing
 * configurations in the ticketing system.
 * It handles operations like retrieving, adding, and deleting configurations.
 */
@RestController
@RequestMapping("/api/configs")
public class ConfigurationController {

    /** The repository that handles CRUD operations for ConfigurationEntity. */
    @Autowired
    private ConfigurationRepository configurationRepository;

    /**
     * Gets a list of all configurations.
     *
     * @return ResponseEntity containing a list of all ConfigurationEntity objects.
     */
    @GetMapping("/getAll")
    public ResponseEntity<List<ConfigurationEntity>> getAllConfigurations() {
        List<ConfigurationEntity> configurations = configurationRepository.findAll();
        return ResponseEntity.ok(configurations);
    }

    /**
     * Adds a new configuration to the system.
     *
     * @param config The ConfigurationEntity object to be added to the system.
     * @return ResponseEntity containing the saved ConfigurationEntity object, or an error status.
     */
    @PostMapping("/add")
    public ResponseEntity<ConfigurationEntity> addConfiguration(@RequestBody ConfigurationEntity config) {
        try {
            // Save the configuration and return it
            ConfigurationEntity savedConfig = configurationRepository.save(config);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedConfig);
        } catch (Exception e) {
            // Return internal server error if something goes wrong
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Deletes a configuration by its ID.
     *
     * @param configId The ID of the configuration to delete.
     * @return ResponseEntity containing a message confirming deletion or indicating failure.
     */
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/delete/{configId}")
    public ResponseEntity<String> deleteConfiguration(@PathVariable Long configId) {
        // Check if the configuration exists
        if (!configurationRepository.existsById(configId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Configuration with ID " + configId + " not found.");
        }
        // Delete the configuration
        configurationRepository.deleteById(configId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Configuration with ID " + configId + " has been deleted.");
    }

    /**
     * Retrieves a configuration by its ID.
     *
     * @param id The ID of the configuration to retrieve.
     * @return ResponseEntity containing the requested ConfigurationEntity if found, otherwise not found status.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<ConfigurationEntity> getConfiguration(@PathVariable Long id) {
        Optional<ConfigurationEntity> requestedConfig = configurationRepository.findById(id);
        if (requestedConfig.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(requestedConfig.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
