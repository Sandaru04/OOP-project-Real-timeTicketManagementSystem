package com.ticketingSystem.backend.service;

import com.ticketingSystem.backend.model.ConfigurationEntity;
import com.ticketingSystem.backend.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for handling operations related to {@link ConfigurationEntity}.
 * It provides methods to create, update, retrieve, and delete configurations.
 */
@Service
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    /**
     * Constructor that initializes the ConfigurationService with the given {@link ConfigurationRepository}.
     *
     * @param configurationRepository the repository to interact with {@link ConfigurationEntity} entities
     */
    @Autowired
    public ConfigurationService(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    /**
     * Saves or updates the given {@link ConfigurationEntity}.
     * If the entity already exists (based on its ID), it will be updated; otherwise, a new entity will be created.
     *
     * @param configuration the {@link ConfigurationEntity} to save or update
     * @return the saved or updated {@link ConfigurationEntity}
     * @throws IllegalArgumentException if the configuration validation fails
     */
    public ConfigurationEntity saveConfiguration(ConfigurationEntity configuration) {
        validateConfiguration(configuration); // Optional validation logic
        return configurationRepository.save(configuration);
    }

    /**
     * Retrieves all {@link ConfigurationEntity} records.
     *
     * @return a list of all {@link ConfigurationEntity} objects
     */
    public List<ConfigurationEntity> getAllConfigurations() {
        return configurationRepository.findAll();
    }

    /**
     * Retrieves a specific {@link ConfigurationEntity} by its ID.
     *
     * @param id the ID of the configuration entity to retrieve
     * @return an {@link Optional} containing the found configuration or an empty {@link Optional} if not found
     */
    public Optional<ConfigurationEntity> getConfigurationById(Long id) {
        return configurationRepository.findById(id);
    }

    /**
     * Deletes a {@link ConfigurationEntity} by its ID.
     *
     * @param id the ID of the configuration entity to delete
     */
    public void deleteConfigurationById(Long id) {
        configurationRepository.deleteById(id);
    }

    /**
     * Validates the provided {@link ConfigurationEntity} to ensure it meets business rules.
     * Throws an {@link IllegalArgumentException} if any validation condition fails.
     *
     * @param configuration the {@link ConfigurationEntity} to validate
     * @throws IllegalArgumentException if the configuration fails validation
     */
    private void validateConfiguration(ConfigurationEntity configuration) {
        if (configuration.getNumberOfVendors() <= 0) {
            throw new IllegalArgumentException("Number of vendors must be greater than 0.");
        }
        if (configuration.getTotalTickets() < configuration.getNumberOfVendors()) {
            throw new IllegalArgumentException("Total tickets must be at least equal to the number of vendors.");
        }
    }
}
