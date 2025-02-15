import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class SystemLogger {
    private static final Logger logger = Logger.getLogger(SystemLogger.class.getName());
    private static boolean initialized = false; // Flag for initialization

    // Constructor (avoids initialization)
    public SystemLogger() {
    }

    // Initialize logger with file and console handlers (called once)
    private synchronized static void initializeLogger() {
        if (!initialized) {
            try {
                // Create FileHandler for logging to a file (append mode)
                FileHandler fileHandler = new FileHandler("D:/Degree/Second year/SEM 1/OOP/Assignment oop/Firestone", true);
                fileHandler.setLevel(Level.ALL);

                // Create ConsoleHandler for logging to the console
                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setLevel(Level.ALL);

                // Apply the same formatter to both handlers
                Formatter formatter = new Formatter() {
                    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");

                    @Override
                    public String format(LogRecord record) {
                        return dateFormat.format(new Date(record.getMillis())) + " " + record.getMessage() + "\n";
                    }
                };
                fileHandler.setFormatter(formatter);
                consoleHandler.setFormatter(formatter);

                // Add both handlers to the logger
                logger.addHandler(fileHandler);
                logger.addHandler(consoleHandler);

                // Disable default console logging
                logger.setUseParentHandlers(false);

                initialized = true; // Mark initialization complete
            } catch (IOException e) {
                System.out.println("Error setting up logging: " + e.getMessage());
            }
        }
    }

    // Method to log messages (calls initializeLogger if not done)
    public void logStatement(String message) {
        initializeLogger(); // Ensure initialization
        logger.info(message);
    }


//    // Method to log messages
//    public void logStatement(String message) {
//        logger.info(message);
//    }


    public void logError(String message) {
        logger.warning(message);
    }

    public void logWarning(String message) {
        logger.severe(message);
    }

    public void separateLog(){
        logger.info("\n");
    }
}