package com.ticketingSystem.backend.logic;

import com.ticketingSystem.backend.web_socket.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The Customer class represents a customer in the ticketing system.
 * It handles the process of a customer attempting to remove tickets from the TicketPool.
 * The customer operates in its own thread, where it continuously tries to retrieve tickets from the pool.
 */
@Component
public class Customer implements Runnable {

    @Autowired
    private WebSocketController webSocketController; // WebSocket controller for sending real-time updates

    private int retrievalRate; // Rate at which the customer tries to retrieve tickets
    private int customerID; // Unique ID assigned to the customer
    private final TicketPool ticketPool; // The pool of tickets that customers can retrieve tickets from
    private int removedTicketsCount = 0; // The total count of tickets removed by this customer

    /**
     * Constructor for the Customer class. The customer is associated with a TicketPool.
     *
     * @param ticketPool the TicketPool from which the customer will remove tickets
     */
    public Customer(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    /**
     * Configures the customer with the retrieval rate and unique customer ID.
     *
     * @param retrievalRate the rate at which the customer tries to retrieve tickets
     * @param customerID the unique identifier for the customer
     */
    public void configure(int retrievalRate, int customerID) {
        this.retrievalRate = retrievalRate;
        this.customerID = customerID;
    }

    /**
     * The run method that defines the behavior of the customer thread. This method continuously
     * tries to remove tickets from the TicketPool while the system is running or until there are no
     * more tickets available.
     */
    @Override
    public void run() {
        while (TicketingSystem.isRunning() || ticketPool.hasTickets()) {
            // Attempt to remove a ticket from the pool
            boolean isSuccess = ticketPool.removeTickets(customerID);
            if (isSuccess) {
                removedTicketsCount++; // Increment the ticket count if successful
            }

            try {
                Thread.sleep(retrievalRate); // Pause the thread for the specified retrieval rate
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle interruption and log the issue
                System.out.println("Customer interrupted! Customer ID: " + customerID);
                break;
            }
        }

        // Log the end of the customer's thread if no tickets are available
        System.out.println("No more tickets available. Customer thread ending. Customer ID: " + customerID);

        // Send a WebSocket message notifying that the customer has quit due to no tickets being available
        if (!(this.webSocketController == null)) {
            webSocketController.sendTicketMessage("No more tickets! Customer ID: " + customerID + " Quit");
        }
    }

    /**
     * Returns the number of tickets removed by this customer.
     *
     * @return the count of tickets the customer has removed
     */
    public int getRemovedTicketsCount() {
        return removedTicketsCount;
    }

    /**
     * Returns the unique ID of the customer.
     *
     * @return the customer ID
     */
    public int getCustomerID() {
        return customerID;
    }
}
