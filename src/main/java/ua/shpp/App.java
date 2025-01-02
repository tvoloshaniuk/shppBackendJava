package ua.shpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        //trycatch for all exceptions.
        logger.info("Application started.");
        logger.trace("TRACE: Application is initializing.");
        logger.warn("WARN: just to test warn.");
        logger.error("ERROR: just to test error.");

        logger.debug("DEBUG: Preparing to load properties.");
        var properties = PropertyProcessor.loadPropertiesFromFile(); //side effect
        int min = PropertyProcessor.getPropertyOrPrompt(properties, "min", "Enter a value for min: ");
        int max = PropertyProcessor.getPropertyOrPrompt(properties, "max", "Enter a value for max:");
        int increment = PropertyProcessor.getPropertyOrPrompt(properties, "increment", "Enter a value for increment: ");
        String type = System.getProperty("type", "int");

        logger.debug("min: {}\n max: {}\n increment: {}\n type: {}", min, max, increment, type);
        logger.info(String.valueOf(min));
        logger.info(String.valueOf(max));
        logger.info(String.valueOf(increment));
        logger.info("Number type set to: {}", type);

//        Message message = new Message().setMessage("Привіт, " + properties.getProperty("username", "unknown") + "!");
//        try {
//            OutputProcessor.printMessage(message, type);
//            logger.debug("just test logger.debug: here I can debug how messa");
//        } catch (JsonProcessingException e) {
//            logger.error("Wrong format", e);
//            System.exit(1);
//        }



        logger.info("Application finished.");
    }

}