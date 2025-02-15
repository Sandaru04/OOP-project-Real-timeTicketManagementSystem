package com.ticketingSystem.backend.logic;

import com.ticketingSystem.backend.web_socket.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The TicketPool class is responsible for managing a pool of tickets available for customers to purchase.
 * It provides methods for adding tickets, removing tickets, and managing the synchronization of ticket-related operations.
 */
@Component
public class TicketPool {

    @Autowired
    private WebSocketController webSocketController;

    private final LinkedList<Ticket> tickets = new LinkedList<>();
    private int maxCapacity;
    private final Lock lock = new ReentrantLock(); // Lock for managing synchronization
    private final AtomicInteger ticketID = new AtomicInteger(1); // Atomic integer for generating unique ticket IDs

    /**
     * Configures the maximum capacity of the ticket pool.
     *
     * @param maxCapacity the maximum number of tickets that the pool can hold
     */
    public void configure(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * Resets the ticket ID counter to 1.
     */
    public void resetTicketID() {
        ticketID.set(1);  // Reset the ticket ID to 1
    }

    /**
     * Adds a ticket to the pool. A ticket is created with a unique ID and added to the pool.
     * If the pool is full, no tickets are added.
     *
     * @param vendorID the ID of the vendor adding the ticket
     * @return true if the ticket was successfully added, false if the pool is full
     */
    public boolean addTickets(int vendorID) {
        lock.lock();  // Acquire the lock to ensure thread safety
        try {
            if (tickets.size() >= maxCapacity) {  // Check if the pool is full
                System.out.println("Ticket Pool is full for Vendor ID: " + vendorID);
                webSocketController.sendTicketMessage("Ticket pool is full for: " + vendorID);
                return false;
            }

            int currentTicketID = getNextTicketID();  // Get the next ticket ID
            Ticket newTicket = new Ticket(currentTicketID);  // Create a new ticket
            tickets.add(newTicket);  // Add the ticket to the pool

            // Log and send messages to the WebSocket controller
            System.out.println("Vendor ID: " + vendorID + " added 1 Ticket to the pool.");
            webSocketController.sendTicketMessage("Vendor ID: " + vendorID + " added 1 Ticket");
            System.out.println("Added Ticket ID: " + currentTicketID + " Event Name: " + newTicket.getTicketName());
            webSocketController.sendTicketMessage("Ticket ID: " + currentTicketID + " Event Name: " + newTicket.getTicketName());
            System.out.println("Current Ticket Pool size: " + tickets.size());

            return true;
        } finally {
            lock.unlock();  // Release the lock
        }
    }

    /**
     * Retrieves the next available ticket ID by incrementing the ticket ID counter.
     *
     * @return the next ticket ID
     */
    private int getNextTicketID() {
        return ticketID.getAndIncrement();  // Atomically get and increment the ticket ID
    }

    /**
     * Removes a ticket from the pool for a customer to purchase.
     * If the pool is empty, no ticket can be removed.
     *
     * @param customerID the ID of the customer removing the ticket
     * @return true if a ticket was successfully removed, false if the pool is empty
     */
    public boolean removeTickets(int customerID) {
        lock.lock();  // Acquire the lock to ensure thread safety
        try {
            if (tickets.isEmpty()) {  // Check if the pool is empty
                System.out.println("Ticket Pool is empty for Customer ID: " + customerID);
                webSocketController.sendTicketMessage("Ticket pool is empty for customer ID: " + customerID);
                return false;
            }

            Ticket removedTicket = tickets.remove(0);  // Remove the first ticket from the pool

            // Log and send messages to the WebSocket controller
            System.out.println("Customer ID: " + customerID + " removed 1 Ticket from the pool.");
            webSocketController.sendTicketMessage("Customer ID: " + customerID + " removed 1 Ticket");
            System.out.println("Removed Ticket ID: " + removedTicket.getTicketID() + " Event Name: " + removedTicket.getTicketName());
            webSocketController.sendTicketMessage("Ticket ID: " + removedTicket.getTicketID() + " Event Name: " + removedTicket.getTicketName());

            return true;
        } finally {
            lock.unlock();  // Release the lock
        }
    }

    /**
     * Checks if there are any tickets available in the pool.
     *
     * @return true if there are tickets available, false if the pool is empty
     */
    public boolean hasTickets() {
        lock.lock();  // Acquire the lock to ensure thread safety
        try {
            return !tickets.isEmpty();  // Return true if there are tickets in the pool
        } finally {
            lock.unlock();  // Release the lock
        }
    }

    /**
     * Gets the current number of tickets in the pool.
     *
     * @return the current size of the ticket pool
     */
    public int getPoolSize() {
        lock.lock();  // Acquire the lock to ensure thread safety
        try {
            return tickets.size();  // Return the size of the ticket pool
        } finally {
            lock.unlock();  // Release the lock
        }
    }

    /**
     * Resets the ticket pool by clearing all tickets.
     */
    public void resetTicketPool() {
        lock.lock();  // Acquire the lock to ensure thread safety
        try {
            tickets.clear();  // Clear all tickets from the pool
        } finally {
            lock.unlock();  // Release the lock
        }
    }
}
