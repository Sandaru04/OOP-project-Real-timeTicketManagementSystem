package com.ticketingSystem.backend.polling;

import com.ticketingSystem.backend.logic.TicketPool;
import com.ticketingSystem.backend.logic.TicketingSystem;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * The PollingService class performs periodic polling tasks to fetch and process data.
 * It runs a scheduled task that retrieves relevant system information and could be extended
 * to perform additional operations at regular intervals.
 */
@Service
public class PollingService {

    private static final Logger logger = Logger.getLogger(PollingService.class.getName());

    private final TicketPool ticketPool;

    public PollingService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    /**
     * Scheduled method that runs every 1 second to fetch the current state of the ticket pool
     * and whether the ticketing system is running.
     * This method can be extended for additional periodic operations.
     */
    @Scheduled(fixedRate = 1000) // Poll every 1 second
    public void fetchData() {
        int poolSize = ticketPool.getPoolSize();
        boolean isSystemRunning = TicketingSystem.isRunning();
    }
}
