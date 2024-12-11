package ua.shpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;


import java.nio.file.Path;

public class PropertyProcessor {
    private static final String PROPERTIES_FILENAME = "username.properties";
    private static final Logger logger = LoggerFactory.getLogger(PropertyProcessor.class);
    public static Properties loadPropertiesFromFile() {
        Properties properties = new Properties();

        //todo classpath support classLoader
        /*Path filePath = Paths.get(PROPERTIES_FILENAME); // шукає у робочій директорії
        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(filePath), StandardCharsets.UTF_8)) {
            properties.load(reader);
            logger.info("The properties are loaded from an external file: {}", PROPERTIES_FILENAME);
        } catch (IOException ex) {
            String exceptionMessage = "File not found or unavailable: " + filePath.toAbsolutePath();
            logger.error(exceptionMessage, ex);
            throw new CriticalResourceException(exceptionMessage, ex);
        }
        return properties;*/


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

        /*try (InputStream inputStream = PropertyProcessor.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME)) {
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
        }*/




        return properties;
    }


}

