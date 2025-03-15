package com.ticketingSystem.backend.logic;

import org.springframework.stereotype.Component;

@Component
public class Customer implements Runnable {

    private int retrievalRate;
    private int customerID;
    private final TicketPool ticketPool;
    private int removedTicketsCount = 0;

    public Customer(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    public void configure(int retrievalRate, int customerID) {
        this.retrievalRate = retrievalRate;
        this.customerID = customerID;
    }

    @Override
    public void run() {
        while (TicketingSystem.isRunning() || ticketPool.hasTickets()) {
            boolean isSuccess = ticketPool.removeTickets(customerID);
            if (isSuccess) {
                removedTicketsCount++;
            }
            try {
                Thread.sleep(retrievalRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Customer interrupted! Customer ID: " + customerID);
                break;
            }
        }
    }

    public int getRemovedTicketsCount() {
        return removedTicketsCount;
    }

    public int getCustomerID() {
        return customerID;
    }
}