package com.ticketingSystem.backend.web_socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket configuration for enabling communication between the client and server using WebSockets.
 * This configuration sets up message brokering and endpoint registration for WebSocket connections.
 */
@Configuration
@EnableWebSocketMessageBroker // Enables WebSocket message handling, backed by a message broker.
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures the message broker for WebSocket communication.
     * The message broker allows the server to push messages to clients via specific topic destinations.
     *
     * @param config the MessageBrokerRegistry used to configure the message broker
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enables a simple message broker for handling messages sent to the "/topic" destinations
        config.enableSimpleBroker("/topic");

        // Sets the prefix for application-bound messages. Messages sent to destinations starting with "/app"
        // will be routed to the application controller.
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registers the STOMP endpoints for WebSocket communication.
     * This defines the WebSocket endpoint where clients can connect and sets allowed origins.
     *
     * @param registry the StompEndpointRegistry used to register the STOMP endpoints
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registers the "/websocket" endpoint, which clients will use to establish WebSocket connections.
        // It allows connections from "http://localhost:3000" (e.g., the front-end client during development).
        registry.addEndpoint("/websocket").setAllowedOrigins("http://localhost:3000").withSockJS();
    }
}
