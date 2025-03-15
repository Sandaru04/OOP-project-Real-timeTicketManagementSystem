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

@Service
public class TicketingSystem {

    @Autowired
    private WebSocketController webSocketController;

    public static AtomicBoolean running = new AtomicBoolean(false);
    public static AtomicInteger finishedVendors = new AtomicInteger(0);
    private static ConfigurationEntity currentConfig;

    private final TicketPool ticketPool;
    private final ThreadManager threadManager;

    @Autowired
    public TicketingSystem(TicketPool ticketPool, ThreadManager threadManager) {
        this.ticketPool = ticketPool;
        this.threadManager = threadManager;
    }

    public void startSystem(ConfigurationEntity config) {
        currentConfig = config;
        running.set(true);
        resetSystem();
        ticketPool.configure(config.getMaxTicketCapacity());

        for (int i = 0; i < config.getNumberOfVendors(); i++) {
            Vendor vendor = new Vendor(ticketPool);
            vendor.configure(config.getTicketReleaseRate(), config.getTotalTickets() / config.getNumberOfVendors(), i + 1);
            threadManager.addVendor(vendor);
        }

        for (int i = 0; i < config.getNumberOfCustomers(); i++) {
            Customer customer = new Customer(ticketPool);
            customer.configure(config.getCustomerRetrievalRate(), i + 1);
            threadManager.addCustomer(customer);
        }

        new Thread(() -> {
            while (running.get()) {
                if (finishedVendors.get() >= config.getNumberOfVendors() && ticketPool.getPoolSize() == 0) {
                    running.set(false);
                    String soldOutMessage = "All tickets sold out! Customers purchasing tickets if remaining...";
                    System.out.println(soldOutMessage);
                    if (webSocketController != null) webSocketController.sendTicketMessage(soldOutMessage);
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            completeCustomers(threadManager.getCustomerThreads());
            Map<String, Object> summaryMap = getSimulationSummary(threadManager.getCustomers(), threadManager.getVendors(), finishedVendors);
            String summary = formatSummary(summaryMap);
            System.out.println(summary);
            if (webSocketController != null) {
                webSocketController.sendTicketMessage(summary);
                webSocketController.sendTicketMessage("Simulation ended");
            }
        }).start();
    }

    private void completeCustomers(List<Thread> customerThreads) {
        for (Thread thread : customerThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Error joining threads: " + e.getMessage());
            }
        }
    }

    private String formatSummary(Map<String, Object> summaryMap) {
        StringBuilder summary = new StringBuilder("Simulation Summary:\n");
        summaryMap.forEach((key, value) -> {
            summary.append(key).append(": ").append(value).append("\n");
        });
        return summary.toString().trim();
    }

    public static Map<String, Object> getSimulationSummary(List<Customer> customers, List<Vendor> vendors, AtomicInteger finishedVendors) {
        Map<String, Object> summary = new HashMap<>();
        int totalTicketsSold = 0;

        for (Customer customer : customers) {
            int ticketsRemoved = customer.getRemovedTicketsCount();
            totalTicketsSold += ticketsRemoved;
            summary.put("Customer " + customer.getCustomerID(), ticketsRemoved);
        }

        for (Vendor vendor : vendors) {
            int ticketsSold = vendor.getSoldTicketCount();
            summary.put("Vendor " + vendor.getVendorID(), ticketsSold);
        }

        summary.put("Total tickets sold", totalTicketsSold);
        summary.put("Total customers served", customers.size());
        summary.put("Total vendors used", finishedVendors.get());

        return summary;
    }

    public static boolean isRunning() {
        return running.get();
    }

    public static ConfigurationEntity getCurrentConfig() {
        return currentConfig;
    }

    public void resetSystem() {
        finishedVendors.set(0);
        ticketPool.resetTicketID();
        ticketPool.resetTicketPool();
    }
}