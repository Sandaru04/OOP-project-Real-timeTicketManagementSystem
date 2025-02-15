import java.util.Vector;
import java.util.logging.Logger;

class TicketPool {
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());
    private final Vector<String> tickets = new Vector<>();
    private final int maxCapacity;
    private int totalTicketsAdded = 0;
    private final int totalTickets;

    public TicketPool(int maxCapacity, int totalTickets) {
        this.maxCapacity = maxCapacity;
        this.totalTickets = totalTickets;
    }

    public synchronized boolean addTicket(String ticket) {
        if (totalTicketsAdded < totalTickets && tickets.size() < maxCapacity) {
            tickets.add(ticket);
            totalTicketsAdded++;
            notifyAll(); // Notify customers waiting for tickets
            return true;
        }
        return false;
    }

    public synchronized String purchaseTicket() {
        while (tickets.isEmpty()) {
            if (totalTicketsAdded >= totalTickets) {
                // No more tickets will be added
                return null;
            }
            try {
                wait(); // Wait until a ticket is available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return tickets.remove(0);
    }

    public synchronized int getRemainingTickets() {
        return tickets.size();
    }
}
