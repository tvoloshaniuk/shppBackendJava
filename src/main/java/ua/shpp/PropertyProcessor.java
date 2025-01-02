package ua.shpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;


class PropertyProcessor {
    private static final String PROPERTIES_FILENAME = "multiplicationTable.properties";
    private static final Logger logger = LoggerFactory.getLogger(PropertyProcessor.class);
    public static Properties loadPropertiesFromFile() {
        Properties properties = new Properties();

        // We use ClassLoader to search for a file in the classpath
        try (InputStream inputStream =  Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_FILENAME)) {
            if (inputStream == null) {
                String exceptionMessage = "File not found in classpath: " + PROPERTIES_FILENAME;
                logger.error(exceptionMessage);
                throw new CriticalResourceException(exceptionMessage, null);
            }
            // We load the properties
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            logger.info("The properties are loaded from classpath: {}", PROPERTIES_FILENAME);
        } catch (IOException ex) {
            String exceptionMessage = "Failed to load properties from classpath: " + PROPERTIES_FILENAME;
            logger.error(exceptionMessage, ex);
            throw new CriticalResourceException(exceptionMessage, ex);
        }


        return properties;
    }

    public static int getPropertyOrPrompt(Properties properties, String key, String prompt) {
        String value = (properties != null) ? properties.getProperty(key) : null;
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                logger.error("Wrong ket format '{}': {}", key, value);
            }
        }

        // alternatively: console input
        Scanner scanner = new Scanner(System.in);
        logger.info(prompt);
        while (!scanner.hasNextInt()) {
            logger.info("Please enter an integer{}", prompt);
            scanner.next();
        }
        return scanner.nextInt();
    }


}

