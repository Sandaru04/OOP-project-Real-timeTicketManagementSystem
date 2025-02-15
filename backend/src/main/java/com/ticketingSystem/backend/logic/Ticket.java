package com.ticketingSystem.backend.logic;

/**
 * The Ticket class represents a ticket in the ticketing system.
 * Each ticket has a unique ID and a name associated with it.
 */
public class Ticket {
    private final int TICKET_ID;
    private final String TICKET_NAME;

    /**
     * Constructs a Ticket object with a given ticket ID.
     * The ticket name is set to a default value "Sample Event".
     *
     * @param ticketID the unique ID of the ticket
     */
    public Ticket(int ticketID) {
        this.TICKET_ID = ticketID;
        this.TICKET_NAME = "Sample Event";  // Default ticket name
    }

    /**
     * Gets the name of the ticket.
     *
     * @return the name of the ticket
     */
    public String getTicketName() {
        return TICKET_NAME;
    }

    /**
     * Gets the unique ID of the ticket.
     *
     * @return the ID of the ticket
     */
    public int getTicketID() {
        return TICKET_ID;
    }
}
