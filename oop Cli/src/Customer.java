import java.util.logging.Logger;

class Customer implements Runnable {
    private static final Logger logger = Logger.getLogger(TicketPool.class.getName());



    private final TicketPool ticketPool;
    private final String customerName;
    private final long retrievalRate;

    public Customer(TicketPool ticketPool, String customerName, long retrievalRate) {
        this.ticketPool = ticketPool;
        this.customerName = customerName;
        this.retrievalRate = retrievalRate;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String ticket = ticketPool.purchaseTicket();
                if (ticket == null) {
                    logger.info(customerName + " couldn't purchase a ticket. No more tickets available.");
                    break; // Stop once all tickets are sold
                }
                logger.info(customerName + " purchased Ticket: " + ticket);
                Thread.sleep(retrievalRate);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}

