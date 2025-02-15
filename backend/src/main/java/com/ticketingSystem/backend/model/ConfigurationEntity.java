package com.ticketingSystem.backend.model;

import jakarta.persistence.*;

/**
 * The ConfigurationEntity class represents the configuration settings for the ticketing system.
 * It is mapped to a database table that stores settings like the number of vendors, customers,
 * ticket release rate, etc. These settings control the behavior of the system during ticket sales.
 */
@Entity
@Table(name = "configuration_entity")
public class ConfigurationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Use IDENTITY for auto-incrementing
    private Long id;  // Unique identifier for each configuration record

    @Column(name = "number_of_vendors")
    private int numberOfVendors;  // Number of vendors in the system

    @Column(name = "total_tickets")
    private int totalTickets;  // Total number of tickets available for sale

    @Column(name = "ticket_release_rate")
    private int ticketReleaseRate;  // Rate at which vendors release tickets (in milliseconds)

    @Column(name = "number_of_customers")
    private int numberOfCustomers;  // Number of customers in the system

    @Column(name = "customer_retrieval_rate")
    private int customerRetrievalRate;  // Rate at which customers retrieve tickets (in milliseconds)

    @Column(name = "max_ticket_capacity")
    private int maxTicketCapacity;  // Maximum capacity of tickets in the pool

    // Getters and setters for each field
    /**
     * Gets the unique identifier for the configuration record.
     *
     * @return the ID of the configuration record
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the number of vendors in the system.
     *
     * @return the number of vendors
     */
    public int getNumberOfVendors() {
        return numberOfVendors;
    }

    /**
     * Gets the total number of tickets available for sale.
     *
     * @return the total tickets
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * Gets the rate at which vendors release tickets (in milliseconds).
     *
     * @return the ticket release rate
     */
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    /**
     * Gets the number of customers in the system.
     *
     * @return the number of customers
     */
    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    /**
     * Gets the rate at which customers retrieve tickets (in milliseconds).
     *
     * @return the customer retrieval rate
     */
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    /**
     * Gets the maximum capacity of tickets in the pool.
     *
     * @return the max ticket capacity
     */
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }
}
