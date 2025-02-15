import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int totalTickets;
    private final String vendorName;
    private final int releaseRate;
    private final AtomicInteger ticketCounter;
    private static final Logger logger = Logger.getLogger(Vendor.class.getName());

    public Vendor(TicketPool ticketPool, int totalTickets, String vendorName, int releaseRate, AtomicInteger ticketCounter) {
        this.ticketPool = ticketPool;
        this.totalTickets = totalTickets;
        this.vendorName = vendorName;
        this.releaseRate = releaseRate;
        this.ticketCounter = ticketCounter;
    }

    @Override
    public void run() {
        for (int i = 1; i <= totalTickets; i++) {
            try {
                String ticket = "T" + ticketCounter.getAndIncrement();
                if (ticketPool.addTicket(ticket)) {
                    logger.info(vendorName + " added a ticket (" + ticket + ")");
                } else {
                    break; // Stop if no more tickets can be added
                }
                Thread.sleep(releaseRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}