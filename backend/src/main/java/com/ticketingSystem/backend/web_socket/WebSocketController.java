package com.ticketingSystem.backend.web_socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.Map;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendTicketMessage(String message) {
        messagingTemplate.convertAndSend("/topic/simulation", message);
    }

    public void sendTicketSummary(Map<String, Object> summaryMap) {
        messagingTemplate.convertAndSend("/topic/simulation", summaryMap);
    }
}