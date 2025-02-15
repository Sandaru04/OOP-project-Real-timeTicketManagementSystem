package com.ticketingSystem.backend.polling;

import com.ticketingSystem.backend.logic.TicketPool;
import com.ticketingSystem.backend.logic.TicketingSystem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * The PollingController class exposes endpoints that allow clients to query the state of the ticketing system.
 * It provides information such as the current size of the ticket pool and whether the ticketing system is running.
 */
@RestController
public class PollingController {

    private final TicketPool ticketPool;

    /**
     * Constructor that initializes the PollingController with a TicketPool instance.
     *
     * @param ticketPool the TicketPool instance to interact with
     */
    public PollingController(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    /**
     * Endpoint to get the current size of the ticket pool.
     * This is a polling mechanism to monitor the number of tickets available.
     *
     * @return a Map containing the status message ("Pool Size") and the current size of the ticket pool
     */
    @GetMapping("/polling/poolSize")
    public Map<String, Integer> getPollingData() {
        // Get the current pool size from the TicketPool
        String poolStatus = "Pool Size";
        int poolSize = ticketPool.getPoolSize();

        // Create a response map and add the pool size value
        Map<String, Integer> result = new HashMap<>();
        result.put(poolStatus, poolSize);
        return result;
    }

    /**
     * Endpoint to check if the ticketing system is currently running.
     * This allows clients to monitor the system's state.
     *
     * @return true if the ticketing system is running, false otherwise
     */
    @GetMapping("/polling/isRunning")
    public boolean isSystemRunning() {
        return TicketingSystem.isRunning();
    }
}
