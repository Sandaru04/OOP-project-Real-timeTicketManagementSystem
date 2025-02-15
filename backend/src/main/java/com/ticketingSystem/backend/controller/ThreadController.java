package com.ticketingSystem.backend.controller;

import com.ticketingSystem.backend.logic.ThreadManager;
import com.ticketingSystem.backend.logic.TicketPool;
import com.ticketingSystem.backend.logic.Vendor;
import com.ticketingSystem.backend.logic.Customer;
import com.ticketingSystem.backend.logic.TicketingSystem;
import com.ticketingSystem.backend.model.ConfigurationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * The ThreadController class provides API endpoints for dynamically managing
 * vendors and customers in the ticketing system during runtime.
 * It allows for adding and removing vendors and customers based on the current configuration.
 */
@RestController
@RequestMapping("/api/dynamic")
public class ThreadController {

    private final ThreadManager threadManager;
    private final TicketPool ticketPool;

    /**
     * Constructor that initializes the ThreadController with required services.
     *
     * @param threadManager The manager responsible for handling threads (vendors and customers).
     * @param ticketPool    The pool responsible for managing the tickets.
     */
    @Autowired
    public ThreadController(ThreadManager threadManager, TicketPool ticketPool, TicketingSystem ticketingSystem) {
        this.threadManager = threadManager;
        this.ticketPool = ticketPool;
    }

    /**
     * Endpoint to add a vendor dynamically based on the current configuration.
     * The new vendor will be added to the thread manager and start handling tickets.
     *
     * @return A response message indicating the result of the operation.
     */
    @PostMapping("/addVendor")
    public String addVendor() {
        if (!TicketingSystem.isRunning()) {
            return "failed";
        }
        ConfigurationEntity config = TicketingSystem.getCurrentConfig();
        if (config != null) {
            // Create and configure a new vendor with the current configuration
            Vendor vendor = new Vendor(ticketPool);
            vendor.configure(config.getTicketReleaseRate(), config.getTotalTickets(), threadManager.getVendorCount() + 1);
            threadManager.addVendor(vendor);
            return "Vendor added. Total vendors: " + threadManager.getVendorCount();
        } else {
            return "Configuration not found. Ensure the system is started first.";
        }
    }

    /**
     * Endpoint to add a customer dynamically based on the current configuration.
     * The new customer will be added to the thread manager and start interacting with the ticket pool.
     *
     * @return A response message indicating the result of the operation.
     */
    @PostMapping("/addCustomer")
    public String addCustomer() {
        if (!TicketingSystem.isRunning()) {
            return "failed";
        }
        ConfigurationEntity config = TicketingSystem.getCurrentConfig();
        if (config != null) {
            // Create and configure a new customer with the current configuration
            Customer customer = new Customer(ticketPool);
            customer.configure(config.getCustomerRetrievalRate(), threadManager.getCustomerCount() + 1);
            threadManager.addCustomer(customer);
            return "Customer added. Total customers: " + threadManager.getCustomerCount();
        } else {
            return "Configuration not found. Ensure the system is started first.";
        }
    }

    // Endpoint to remove a vendor dynamically (commented out, to be implemented later)
    // @PostMapping("/removeVendor")
    // public String removeVendor() {
    //     TicketingSystem.finishedVendors.decrementAndGet();
    //     threadManager.removeVendor();
    //     return "Vendor removed. Total vendors: " + threadManager.getVendorCount();
    // }

    // Endpoint to remove a customer dynamically (commented out, to be implemented later)
    // @PostMapping("/removeCustomer")
    // public String removeCustomer() {
    //     threadManager.removeCustomer();
    //     return "Customer removed. Total customers: " + threadManager.getCustomerCount();
    // }
}
