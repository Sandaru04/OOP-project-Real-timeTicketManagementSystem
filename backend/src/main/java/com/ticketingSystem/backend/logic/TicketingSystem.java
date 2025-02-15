package com.ticketingSystem.backend.logic;

import com.ticketingSystem.backend.model.ConfigurationEntity;
import com.ticketingSystem.backend.web_socket.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The TicketingSystem class manages the overall ticketing process, including the initialization of vendors and customers.
 * It interacts with the TicketPool and ThreadManager to start and stop the system and monitor the simulation.
 */
@Service
public class TicketingSystem {

    @Autowired
    private WebSocketController webSocketController;

    // Atomic variables to manage the state of the system
    public static AtomicBoolean running = new AtomicBoolean(false);
    public static AtomicInteger finishedVendors = new AtomicInteger(0);
    private static ConfigurationEntity currentConfig;

    private final TicketPool ticketPool;
    private final ThreadManager threadManager;

    /**
     * Constructs a TicketingSystem instance.
     *
     * @param ticketPool the ticket pool used for managing available tickets
     * @param threadManager the thread manager used for managing vendor and customer threads
     */
    public TicketingSystem(TicketPool ticketPool, ThreadManager threadManager) {
        this.ticketPool = ticketPool;
        this.threadManager = threadManager;
    }

    /**
     * Starts the ticketing system based on the given configuration.
     * It initializes the ticket pool, starts vendor and customer threads, and monitors the system.
     *
     * @param config the configuration settings for the ticketing system
     */
    public void startSystem(ConfigurationEntity config) {
        currentConfig = config;
        running.set(true);
        resetSystem();  // Resets the system before starting a new simulation
        ticketPool.configure(config.getMaxTicketCapacity());  // Configures the ticket pool

        // Start vendor threads based on the number of vendors in the configuration
        for (int i = 0; i < config.getNumberOfVendors(); i++) {
            Vendor vendor = new Vendor(ticketPool);
            vendor.configure(config.getTicketReleaseRate(), config.getTotalTickets(), i + 1);
            threadManager.addVendor(vendor);  // Add vendor to thread manager
        }

        // Start customer threads based on the number of customers in the configuration
        for (int i = 0; i < config.getNumberOfCustomers(); i++) {
            Customer customer = new Customer(ticketPool);
            customer.configure(config.getCustomerRetrievalRate(), i + 1);
            threadManager.addCustomer(customer);  // Add customer to thread manager
        }

        // Monitor the system while it's running
        while (running.get()) {
            if (finishedVendors.get() == threadManager.getVendorCount()) {
                running.set(false);  // Stop the system when all vendors finish
                System.out.println("\nAll tickets sold out! Customers purchasing tickets if remaining...");
                webSocketController.sendTicketMessage("Tickets sold out! Customer purchasing remaining");
                break;
            }
        }

        // Complete any remaining customer threads
        completeCustomers(threadManager.getCustomerThreads());

        // Print the simulation summary
        Map<String, Object> simulationSummary = getSimulationSummary(threadManager.getCustomers(), threadManager.getVendors(), finishedVendors);
        System.out.println(simulationSummary);
        webSocketController.sendTicketSummary(simulationSummary);
        webSocketController.sendTicketMessage("Simulation ended");
        System.out.println("Simulation ended");
    }

    /**
     * Completes the customer threads by waiting for them to finish using join().
     *
     * @param customerThreads the list of customer threads
     */
    private void completeCustomers(List<Thread> customerThreads) {
        for (Thread thread : customerThreads) {
            try {
                thread.join();  // Wait for each customer thread to finish
            } catch (InterruptedException e) {
                System.out.println("Error joining threads: " + e.getMessage());
            }
        }
    }

    /**
     * Generates a summary of the simulation, including tickets sold and the number of vendors and customers.
     *
     * @param customers the list of customers involved in the simulation
     * @param vendors the list of vendors involved in the simulation
     * @param finishedVendors the number of vendors who have finished selling tickets
     * @return a string representing the simulation summary
     */
    public static Map<String, Object> getSimulationSummary(List<Customer> customers, List<Vendor> vendors, AtomicInteger finishedVendors) {
        Map<String, Object> summary = new HashMap<>();

        int totalTicketsSold = 0;

        // Add customer details to the summary
        for (Customer customer : customers) {
            int ticketsRemoved = customer.getRemovedTicketsCount();
            totalTicketsSold += ticketsRemoved;

            // Using a map to store customer info
            summary.put("Customer " + customer.getCustomerID(), ticketsRemoved);
        }

        // Add vendor details to the summary
        for (Vendor vendor : vendors) {
            int ticketsSold = vendor.getSoldTicketCount();
            // Using a map to store vendor info
            summary.put("Vendor " + vendor.getVendorID(), ticketsSold);
        }

        // Add total summary details to the map
        summary.put("Total tickets sold", totalTicketsSold);
        summary.put("Total customers served", customers.size());
        summary.put("Total vendors used", finishedVendors.get());

        return summary;
    }

    /**
     * Returns the current state of the ticketing system (whether it's running or not).
     *
     * @return true if the system is running, false otherwise
     */
    public static boolean isRunning() {
        return running.get();
    }

    /**
     * Gets the current configuration of the ticketing system.
     *
     * @return the current configuration entity
     */
    public static ConfigurationEntity getCurrentConfig() {
        return currentConfig;
    }

    /**
     * Resets the ticketing system to its initial state.
     * This includes resetting the ticket pool and stopping all threads.
     */
    public void resetSystem() {
        finishedVendors.set(0);
        ticketPool.resetTicketID();
        ticketPool.resetTicketPool();  // Resets the ticket pool
    }
}
