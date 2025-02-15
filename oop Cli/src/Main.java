import com.google.gson.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.*;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        int totalTickets;
        int vendorNum;
        int customerNum;
        int ticketReleaseRate;
        int customerRetrievalRate;
        int maxTicketCapacity;
        Configuration configuration;
        Scanner scanner = new Scanner(System.in);

        // Setup logger
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = new Date();
        logger.info("****** Real-Time Event Ticketing System ******");
        logger.info("Current Date and Time: " + dateFormat.format(currentDate));

        // Ask user if they want to load last configuration
        logger.info("Do you want to load the last user input?");
        logger.info ("Press 1 for Yes");
        logger.info("2 for No (New Input)");
        int choice = validateChoice("Your choice: ", 1, 2);

        if (choice == 1) {
            configuration = loadConfiguration();
            if (configuration != null) {
                logger.info("Loaded previous configuration.");
            } else {
                logger.info("No previous configuration found. Starting fresh.");
                configuration = getNewConfiguration(scanner);
            }
        } else {
            configuration = getNewConfiguration(scanner);
        }

        // Save configuration to JSON
        storeJSON(configuration);
        logger.info("Starting Simulation");

        try {
            runSimulation(configuration);
        } catch (InterruptedException e) {
            logger.severe("Interrupted while threading: " + e.getMessage());
        }

        // Ask if user wants to run again
        logger.info("Do you want to run again? ");
        logger.info("Press 1 to run again");
        logger.info( "Press 2 to exit");
        int runAgainChoice = validateChoice("Your choice:", 1, 2);
        if (runAgainChoice == 1) {
            main(args); // Restart program
        } else {
            logger.info("Exiting program...");
        }
    }

    public static int validateChoice(String statement, int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int user_input;
        while (true) {
            try {
                System.out.print(statement);
                user_input = scanner.nextInt();
                if (user_input < min || user_input > max) {
                    logger.warning("Please choose a valid option: " + min + " or " + max);
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                logger.warning("Enter a valid option: " + min + " or " + max);
                scanner.nextLine();  // Clear buffer
            }
        }
        return user_input;
    }

    public static int validate(String statement) {
        Scanner scanner = new Scanner(System.in);
        int userInput;
        while (true) {
            try {
                System.out.print(statement);
                userInput = scanner.nextInt();
                if (userInput < 0) {
                    logger.warning("Input must be a positive number.");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                logger.warning("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear buffer
            }
        }
        return userInput;
    }

    public static int validate(String statement, int upperLimit) {
        Scanner scanner = new Scanner(System.in);
        int userInput;
        while (true) {
            try {
                System.out.print(statement);
                userInput = scanner.nextInt();
                if (userInput < 0 || userInput > upperLimit) {
                    logger.warning("Input must be between 0 and " + upperLimit);
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                logger.warning("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear buffer
            }
        }
        return userInput;
    }

    public static Configuration loadConfiguration() {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader("configuration.json")) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                JsonElement lastElement = jsonArray.get(jsonArray.size() - 1); // Load last configuration
                return gson.fromJson(lastElement, Configuration.class);
            }
        } catch (IOException e) {
            logger.warning("Error loading previous configuration: " + e.getMessage());
        }
        return null;
    }

    public static Configuration getNewConfiguration(Scanner scanner) {
        int maxTicketCapacity = validate("Enter the maximum Ticket capacity: ");
        int totalTickets = validate("Enter the Total Number of Tickets (<= Maximum Ticket Capacity): ", maxTicketCapacity);
        int vendorNum = validate("Enter the number of Vendors: ");
        int customerNum = validate("Enter the number of customers: ");
        int ticketReleaseRate = validate("Enter the Ticket Release rate (milliseconds): ");
        int customerRetrievalRate = validate("Enter the Customer Retrieval rate in milliseconds (Non VIP): ");
        return new Configuration(totalTickets, vendorNum, customerNum, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
    }

    public static void storeJSON(Configuration configuration) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Configuration> configurationList = new ArrayList<>();

        try (FileReader reader = new FileReader("configuration.json")) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                for (JsonElement element : jsonArray) {
                    Configuration existingConfig = gson.fromJson(element, Configuration.class);
                    configurationList.add(existingConfig);
                }
            }
        } catch (IOException e) {
            logger.info("No existing configuration file found. A new file will be created.");
        }

        configurationList.add(configuration);

        try (FileWriter writer = new FileWriter("configuration.json")) {
            gson.toJson(configurationList, writer);
            logger.info("Configuration successfully recorded.");
        } catch (IOException e) {
            logger.severe("Error recording configuration: " + e.getMessage());
        }
    }

    public static void runSimulation(Configuration configuration) throws InterruptedException {
        TicketPool ticketPool = new TicketPool(configuration.getMaxTicketCapacity(), configuration.getTotalTickets());
        AtomicInteger ticketCounter = new AtomicInteger(1); // Shared counter for unique ticket IDs

        // Start vendors
        for (int i = 0; i < configuration.getVendorNum(); i++) {
            new Thread(new Vendor(ticketPool, configuration.getTotalTickets(), "Vendor_" + i, configuration.getTicketReleaseRate(), ticketCounter)).start();
            logger.info("Vendor " + i + " started ticket release process.");
        }

        Thread.sleep(1000);

        // Start customers
        for (int i = 0; i < configuration.getCustomerNum(); i++) {
            new Thread(new Customer(ticketPool, "Customer_" + i, configuration.getCustomerRetrievalRate())).start();
            logger.info("Customer " + i + " started ticket retrieval.");
        }
    }
}
