package com.ticketingSystem.backend.logic;

import com.ticketingSystem.backend.web_socket.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ThreadManager {

    @Autowired
    private WebSocketController webSocketController;

    private final List<Vendor> vendors = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final List<Thread> vendorThreads = new ArrayList<>();
    private final List<Thread> customerThreads = new ArrayList<>();
    private final Lock vendorLock = new ReentrantLock();
    private final Lock customerLock = new ReentrantLock();

    public void addVendor(Vendor vendor) {
        Thread vendorThread = new Thread(vendor);
        vendorThread.start();
        vendorLock.lock();
        try {
            vendors.add(vendor);
            vendorThreads.add(vendorThread);
            String message = "Vendor thread added. Total vendors: " + getVendorCount();
            System.out.println(message);
            if (webSocketController != null) webSocketController.sendTicketMessage(message);
        } finally {
            vendorLock.unlock();
        }
    }

    public void addCustomer(Customer customer) {
        Thread customerThread = new Thread(customer);
        customerThread.start();
        customerLock.lock();
        try {
            customers.add(customer);
            customerThreads.add(customerThread);
            String message = "Customer thread added. Total customers: " + getCustomerCount();
            System.out.println(message);
            if (webSocketController != null) webSocketController.sendTicketMessage(message);
        } finally {
            customerLock.unlock();
        }
    }

    public int getVendorCount() {
        vendorLock.lock();
        try {
            return vendorThreads.size();
        } finally {
            vendorLock.unlock();
        }
    }

    public int getCustomerCount() {
        customerLock.lock();
        try {
            return customerThreads.size();
        } finally {
            customerLock.unlock();
        }
    }

    public void stopAllThreads() {
        TicketingSystem.running.set(false);
        vendorLock.lock();
        try {
            vendorThreads.forEach(Thread::interrupt);
            vendorThreads.clear();
            vendors.clear();
        } finally {
            vendorLock.unlock();
        }
        customerLock.lock();
        try {
            customerThreads.forEach(Thread::interrupt);
            customerThreads.clear();
            customers.clear();
        } finally {
            customerLock.unlock();
        }
    }

    public List<Thread> getCustomerThreads() {
        customerLock.lock();
        try {
            return new ArrayList<>(customerThreads);
        } finally {
            customerLock.unlock();
        }
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public List<Vendor> getVendors() {
        return vendors;
    }
}