package ua.shpp;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/* ChangeLog:
    done:
       //class for properties processing
       //decompose with classes
       - removed unnecessary properties (for ex: filepath)
       - program falls faster
*/


public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        System.out.println("hello world 2!");
        System.out.println("change in dev, which is absent in master. aster merge request is expected to be added");
        //comment+1

        //trycatch for all exceptions
        logger.info("Application started.");
        logger.trace("TRACE: Application is initializing.");
        logger.warn("WARN: just to test warn.");
        logger.error("ERROR: just to test error.");

        logger.debug("DEBUG: Preparing to load properties.");
        var properties = PropertyProcessor.loadPropertiesFromFile(); //side effect

        String outputFormat = System.getProperty("format", "json");
        logger.info("Output format set to: {}", outputFormat);

        Message message = new Message().setMessage("Привіт, " + properties.getProperty("username", "unknown") + "!");
        try {
            OutputProcessor.printMessage(message, outputFormat);
            logger.debug("just test logger.debug: here I can debug how messa");
        } catch (JsonProcessingException e) {
            logger.error("Wrong format", e);
            System.exit(1);
        }

        logger.info("Application finished.");
    }

}