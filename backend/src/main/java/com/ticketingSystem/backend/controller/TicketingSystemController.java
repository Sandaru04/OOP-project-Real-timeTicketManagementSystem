package com.ticketingSystem.backend.controller;

import com.ticketingSystem.backend.logic.ThreadManager;
import com.ticketingSystem.backend.logic.TicketingSystem;
import com.ticketingSystem.backend.model.ConfigurationEntity;
import com.ticketingSystem.backend.repository.ConfigurationRepository;
import com.ticketingSystem.backend.web_socket.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Controller responsible for handling ticketing system operations
 * such as starting, stopping, and resetting the system.
 */
@RestController
@RequestMapping("/api/ticketing-system")
public class TicketingSystemController {

    @Autowired
    private WebSocketController webSocketController; // WebSocket controller for sending real-time updates

    private final TicketingSystem ticketingSystem; // Instance of the TicketingSystem to manage ticketing logic
    private final ConfigurationRepository configurationRepository; // Repository for accessing configurations
    private final ThreadManager threadManager; // Thread manager for handling vendor and customer threads

    /**
     * Constructor-based dependency injection for the required components.
     *
     * @param ticketingSystem the ticketing system to manage the ticketing process
     * @param configurationRepository the repository to interact with configuration entities
     * @param threadManager the thread manager to handle vendor and customer threads
     */
    @Autowired
    public TicketingSystemController(TicketingSystem ticketingSystem, ConfigurationRepository configurationRepository, ThreadManager threadManager) {
        this.ticketingSystem = ticketingSystem;
        this.configurationRepository = configurationRepository;
        this.threadManager = threadManager;
    }

    /**
     * Endpoint to start the ticketing system using the configuration with the given ID.
     *
     * This method checks if the configuration exists, and if so, starts the ticketing system
     * with the provided configuration. It returns a success response if the system is started,
     * or an error message if the configuration is not found.
     *
     * @param id the ID of the configuration to be used for starting the system
     * @return a ResponseEntity indicating whether the system was successfully started or not
     */
    @PostMapping("/start/{id}")
    public ResponseEntity<String> startSystem(@PathVariable Long id) {
        // Attempt to retrieve the configuration by ID from the repository
        Optional<ConfigurationEntity> configuration = configurationRepository.findById(id);

        // If configuration is found, start the system with it, else return a not found status
        if (configuration.isPresent()) {
            ConfigurationEntity config = configuration.get();
            ticketingSystem.startSystem(config); // Start the system with the given configuration
            return ResponseEntity.status(HttpStatus.OK).body("Ticketing system started with configuration ID: " + id);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Configuration not found");
        }
    }

    /**
     * Endpoint to stop the ticketing system.
     *
     * This method stops all running threads (vendors and customers) and sends a message
     * to the WebSocket controller indicating that the system has been stopped.
     *
     * @return a message indicating that the ticketing system has been stopped
     */
    @PostMapping("/stop")
    public String stopSystem() {
        threadManager.stopAllThreads(); // Stop all active threads related to vendors and customers
        webSocketController.sendTicketMessage("Ticketing system stopped"); // Send a WebSocket message about system stop
        return "Ticketing System Stopped";
    }

    /**
     * Endpoint to reset the ticketing system.
     *
     * This method resets the ticketing system, returning it to its initial state.
     *
     * @return a message indicating that the ticketing system has been reset
     */
    @PostMapping("/reset")
    public String resetSystem() {
        ticketingSystem.resetSystem(); // Reset the system to its initial state
        return "Ticketing System reset";
    }
}
