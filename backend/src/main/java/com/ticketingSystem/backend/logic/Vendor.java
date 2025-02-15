package com.ticketingSystem.backend.logic;

import com.ticketingSystem.backend.web_socket.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The Vendor class represents a vendor responsible for adding tickets to the pool at a defined rate.
 * Each vendor runs in its own thread and adds tickets to the pool until the vendor runs out of tickets to add.
 */
@Component
public class Vendor implements Runnable {

    @Autowired
    private WebSocketController webSocketController;

    private final TicketPool ticketPool;
    private int ticketReleaseRate;  // Rate at which tickets are added (milliseconds between each ticket release)
    private int totalTickets;  // Total tickets the vendor will release
    private int vendorID;  // Unique ID for the vendor
    private int soldTicketCount = 0;  // Count of tickets sold by this vendor

    /**
     * Constructor for the Vendor class.
     *
     * @param ticketPool the pool where tickets will be added
     */
    public Vendor(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    /**
     * Configures the vendor with the ticket release rate, total tickets to release, and a unique vendor ID.
     *
     * @param ticketReleaseRate the rate at which the vendor releases tickets (in milliseconds)
     * @param totalTickets the total number of tickets this vendor will release
     * @param vendorID the unique identifier for the vendor
     */
    public void configure(int ticketReleaseRate, int totalTickets, int vendorID) {
        this.ticketReleaseRate = ticketReleaseRate;
        this.totalTickets = totalTickets;
        this.vendorID = vendorID;
    }

    /**
     * The run method is the entry point for the vendor's thread. It adds tickets to the pool until
     * the vendor runs out of tickets to release or the system stops running.
     */
    @Override
    public void run() {
        while (TicketingSystem.isRunning() && totalTickets > 0) {
            // Attempt to add a ticket to the pool
            boolean isSuccess = ticketPool.addTickets(vendorID);
            if (isSuccess) {
                totalTickets--;  // Decrease the total tickets this vendor can add
                soldTicketCount++;  // Increment the count of sold tickets for this vendor
            }

            // If all tickets have been sold, mark the vendor as finished
            if (totalTickets == 0) {
                TicketingSystem.finishedVendors.incrementAndGet();
            }

            // Log remaining tickets for the vendor
            System.out.println("Remaining tickets: " + totalTickets + " for Vendor ID: " + vendorID);
            if (webSocketController != null) {
                webSocketController.sendTicketMessage("Remaining tickets: " + totalTickets + " for Vendor ID: " + vendorID);
            }

            try {
                Thread.sleep(ticketReleaseRate);  // Wait before releasing the next ticket
            } catch (InterruptedException e) {
                // Handle thread interruption
                Thread.currentThread().interrupt();
                System.out.println("Vendor interrupted. Vendor ID: " + vendorID);
                break;  // Exit the loop if interrupted
            }
        }
    }

    /**
     * Returns the total number of tickets sold by this vendor.
     *
     * @return the number of tickets sold by this vendor
     */
    public int getSoldTicketCount() {
        return soldTicketCount;
    }

    /**
     * Returns the unique identifier for this vendor.
     *
     * @return the vendor ID
     */
    public int getVendorID() {
        return vendorID;
    }
}
