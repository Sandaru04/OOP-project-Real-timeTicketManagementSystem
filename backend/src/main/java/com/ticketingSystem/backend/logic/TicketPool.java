package com.ticketingSystem.backend.logic;

import com.ticketingSystem.backend.web_socket.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class TicketPool {

    @Autowired
    private WebSocketController webSocketController;

    private final LinkedList<Ticket> tickets = new LinkedList<>();
    private int maxCapacity;
    private final Lock lock = new ReentrantLock();
    private final AtomicInteger ticketID = new AtomicInteger(1);

    public void configure(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void resetTicketID() {
        ticketID.set(1);
    }

    public boolean addTickets(int vendorID) {
        lock.lock();
        try {
            if (tickets.size() >= maxCapacity) {
                String message = "Ticket pool is full for Vendor ID: " + vendorID;
                System.out.println(message);
                if (webSocketController != null) webSocketController.sendTicketMessage(message);
                return false;
            }

            int currentTicketID = getNextTicketID();
            Ticket newTicket = new Ticket(currentTicketID);
            tickets.add(newTicket);

            String addMessage = String.format("Vendor ID: %d added 1 Ticket to the pool.\nAdded Ticket ID: %d Event Name: %s",
                    vendorID, currentTicketID, newTicket.getTicketName());
            System.out.println(addMessage);
            if (webSocketController != null) webSocketController.sendTicketMessage(addMessage);

            return true;
        } finally {
            lock.unlock();
        }
    }

    private int getNextTicketID() {
        return ticketID.getAndIncrement();
    }

    public boolean removeTickets(int customerID) {
        lock.lock();
        try {
            if (tickets.isEmpty()) {
                String message = "Ticket pool is empty for Customer ID: " + customerID;
                System.out.println(message);
                if (webSocketController != null) webSocketController.sendTicketMessage(message);
                return false;
            }

            Ticket removedTicket = tickets.remove(0);

            String removeMessage = String.format("Customer ID: %d removed 1 Ticket from the pool.\nRemoved Ticket ID: %d Event Name: %s",
                    customerID, removedTicket.getTicketID(), removedTicket.getTicketName());
            System.out.println(removeMessage);
            if (webSocketController != null) webSocketController.sendTicketMessage(removeMessage);

            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean hasTickets() {
        lock.lock();
        try {
            return !tickets.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    public int getPoolSize() {
        lock.lock();
        try {
            return tickets.size();
        } finally {
            lock.unlock();
        }
    }

    public void resetTicketPool() {
        lock.lock();
        try {
            tickets.clear();
        } finally {
            lock.unlock();
        }
    }
}