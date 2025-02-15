package com.ticketingSystem.backend.logic;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The ThreadManager class is responsible for managing the vendor and customer threads in the ticketing system.
 * It adds, removes, and tracks the vendor and customer threads to control the flow of the ticketing process.
 * Thread synchronization is handled using locks to prevent race conditions when modifying shared resources.
 */
@Service
public class ThreadManager {
    private final List<Vendor> vendors = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();

    private final List<Thread> vendorThreads = new ArrayList<>();
    private final List<Thread> customerThreads = new ArrayList<>();

    private final Lock vendorLock = new ReentrantLock();
    private final Lock customerLock = new ReentrantLock();

    /**
     * Adds a Vendor thread to the system and starts its execution.
     * The Vendor thread will continuously add tickets to the ticket pool.
     *
     * @param vendor the Vendor instance to be added and run
     */
    public void addVendor(Vendor vendor) {
        Thread vendorThread = new Thread(vendor);  // Vendor implements Runnable
        vendorThread.start();  // Start the thread
        vendorLock.lock();
        try {
            vendors.add(vendor);
            vendorThreads.add(vendorThread);  // Add to the vendor threads list
        } finally {
            vendorLock.unlock();
        }
        System.out.println("Vendor thread added. Total vendors: " + getVendorCount());
    }

    /**
     * Adds a Customer thread to the system and starts its execution.
     * The Customer thread will continuously attempt to remove tickets from the ticket pool.
     *
     * @param customer the Customer instance to be added and run
     */
    public void addCustomer(Customer customer) {
        Thread customerThread = new Thread(customer);  // Customer implements Runnable
        customerThread.start();  // Start the thread
        customerLock.lock();
        try {
            customers.add(customer);
            customerThreads.add(customerThread);  // Add to the customer threads list
        } finally {
            customerLock.unlock();
        }
        System.out.println("Customer thread added. Total customers: " + getCustomerCount());
    }

    /**
     * Retrieves the current number of vendor threads running.
     *
     * @return the count of currently active vendor threads
     */
    public int getVendorCount() {
        vendorLock.lock();
        try {
            return vendorThreads.size();
        } finally {
            vendorLock.unlock();
        }
    }

    /**
     * Retrieves the current number of customer threads running.
     *
     * @return the count of currently active customer threads
     */
    public int getCustomerCount() {
        customerLock.lock();
        try {
            return customerThreads.size();
        } finally {
            customerLock.unlock();
        }
    }

    /**
     * Stops all vendor and customer threads, and marks the ticketing system as no longer running.
     * This method interrupts all running threads and clears the list of vendor and customer threads.
     */
    public void stopAllThreads() {
        TicketingSystem.running.set(false);  // Mark the ticketing system as stopped
        vendorLock.lock();
        try {
            vendorThreads.forEach(Thread::interrupt);  // Interrupt all vendor threads
            vendorThreads.clear();  // Clear the vendor threads list
            vendors.clear();
        } finally {
            vendorLock.unlock();
        }

        customerLock.lock();
        try {
            customerThreads.forEach(Thread::interrupt);  // Interrupt all customer threads
            customerThreads.clear();  // Clear the customer threads list
            customers.clear();
        } finally {
            customerLock.unlock();
        }
    }

    /**
     * Returns a list of currently active customer threads.
     * This method returns a copy to prevent external modification of the thread list.
     *
     * @return a copy of the list of customer threads
     */
    public List<Thread> getCustomerThreads() {
        customerLock.lock();
        try {
            return new ArrayList<>(customerThreads); // Return a copy to prevent external modification
        } finally {
            customerLock.unlock();
        }
    }

    /**
     * Returns the list of current customer objects. The list contains all customer instances that are running.
     *
     * @return the list of active Customer objects
     */
    public List<Customer> getCustomers() {
        return customers;
    }

    /**
     * Returns the list of current vendor objects. The list contains all vendor instances that are running.
     *
     * @return the list of active Vendor objects
     */
    public List<Vendor> getVendors() {
        return vendors;
    }
}
