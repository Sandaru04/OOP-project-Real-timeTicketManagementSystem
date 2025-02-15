import java.util.logging.Logger;

public class Configuration {
    private static final Logger logger = Logger.getLogger(Configuration.class.getName());

    private int totalTickets;
    private int vendorNum;
    private int customerNum;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private String startDateTime;

    public Configuration(int totalTickets, int vendorNum, int customerNum, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.vendorNum = vendorNum;
        this.customerNum = customerNum;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;

        logger.info("Configuration created: " + this.toString());
    }

    public int getTotalTickets() {
        logger.fine("getTotalTickets() called. Value: " + this.totalTickets);
        return this.totalTickets;
    }

    public int getVendorNum() {
        logger.fine("getVendorNum() called. Value: " + this.vendorNum);
        return this.vendorNum;
    }

    public int getCustomerNum() {
        logger.fine("getCustomerNum() called. Value: " + this.customerNum);
        return this.customerNum;
    }

    public int getTicketReleaseRate() {
        logger.fine("getTicketReleaseRate() called. Value: " + this.ticketReleaseRate);
        return this.ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        logger.fine("getCustomerRetrievalRate() called. Value: " + this.customerRetrievalRate);
        return this.customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        logger.fine("getMaxTicketCapacity() called. Value: " + this.maxTicketCapacity);
        return this.maxTicketCapacity;
    }

    public String getStartDateTime() {
        logger.fine("getStartDateTime() called. Value: " + this.startDateTime);
        return this.startDateTime;
    }

    public void setStartDateTime(String dateTime) {
        this.startDateTime = dateTime;
        logger.info("StartDateTime set to: " + dateTime);
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "totalTickets=" + totalTickets +
                ", vendorNum=" + vendorNum +
                ", customerNum=" + customerNum +
                ", ticketReleaseRate=" + ticketReleaseRate +
                ", customerRetrievalRate=" + customerRetrievalRate +
                ", maxTicketCapacity=" + maxTicketCapacity +
                ", startDateTime='" + startDateTime + '\'' +
                '}';
    }
}

