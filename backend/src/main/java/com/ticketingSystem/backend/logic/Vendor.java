package com.ticketingSystem.backend.logic;

import org.springframework.stereotype.Component;

@Component
public class Vendor implements Runnable {

    private final TicketPool ticketPool;
    private int ticketReleaseRate;
    private int totalTickets;
    private int vendorID;
    private int soldTicketCount = 0;

    public Vendor(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    public void configure(int ticketReleaseRate, int totalTickets, int vendorID) {
        this.ticketReleaseRate = ticketReleaseRate;
        this.totalTickets = totalTickets;
        this.vendorID = vendorID;
    }

    @Override
    public void run() {
        while (TicketingSystem.isRunning() && totalTickets > 0) {
            boolean isSuccess = ticketPool.addTickets(vendorID);
            if (isSuccess) {
                totalTickets--;
                soldTicketCount++;
            }
            if (totalTickets == 0) {
                TicketingSystem.finishedVendors.incrementAndGet();
            }
            try {
                Thread.sleep(ticketReleaseRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Vendor interrupted. Vendor ID: " + vendorID);
                break;
            }
        }
    }

    public int getSoldTicketCount() {
        return soldTicketCount;
    }

    public int getVendorID() {
        return vendorID;
    }
}