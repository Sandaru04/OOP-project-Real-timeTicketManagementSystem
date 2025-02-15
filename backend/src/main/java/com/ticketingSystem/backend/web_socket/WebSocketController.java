package com.ticketingSystem.backend.web_socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.Map;

/**
 * Controller responsible for managing WebSocket communication with the front end.
 * This class handles sending ticket-related messages to clients connected via WebSocket.
 */
@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Sends a ticket-related message to the WebSocket topic for the simulation.
     * This method is used to broadcast messages related to the ticketing system to connected clients.
     *
     * @param message the message to send to the WebSocket clients
     */
    public void sendTicketMessage(String message) {
        // Sends the provided message to the "/topic/simulation" WebSocket topic
        messagingTemplate.convertAndSend("/topic/simulation", message);
    }

    /**
     * Sends a map of ticket-related data to the WebSocket topic for the simulation.
     * This method sends more structured information, such as statistics or other data related to the ticketing system.
     *
     * @param summaryMap a Map containing the summary data to send to the clients
     */
    public void sendTicketSummary(Map<String, Object> summaryMap) {
        // Sends the summary map to the "/topic/simulation" WebSocket topic
        messagingTemplate.convertAndSend("/topic/simulation", summaryMap);
    }
}
