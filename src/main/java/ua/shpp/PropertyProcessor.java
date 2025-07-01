package ua.shpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/* Helps to import and process values into App from property file and from System  */
class PropertyProcessor {
    private final String PROPERTIES_FILENAME;
    protected static final Logger logger = LoggerFactory.getLogger(PropertyProcessor.class);
    private static final Set<String> SUPPORTED_TYPES = Set.of(
            "byte", "double", "float", "int", "long", "short"
    );
    private Properties properties = new Properties();

    PropertyProcessor(String properties_filename) {
        this.PROPERTIES_FILENAME = properties_filename;
        loadPropertiesFromFile();
    }

    private void loadPropertiesFromFile() {
        // We use ClassLoader to search for a file in the classpath
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(PROPERTIES_FILENAME)) {
            if (inputStream == null) {
                String msg = "File not found in classpath: " + PROPERTIES_FILENAME;
                throw new CriticalResourceException(List.of(msg));
            }
            // We load the properties
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            logger.info("The properties are loaded from classpath: {}", PROPERTIES_FILENAME);
        } catch (IOException ex) {
            String msg = "Failed to load properties from classpath: " + PROPERTIES_FILENAME;
            throw new CriticalResourceException(List.of(msg, ex.getMessage()));
        }

    }

    public Number getNumericProperty(String key, String type) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return parseStringToType(value, type);
            } catch (Exception e) {
                String msg = "Value '" + value + "' in the file was not recognised as a Number of type '" + type + "'";
                throw new IllegalArgumentException(msg, e);
            }
        } else {
            String msg = "Variable '" + key + "' is missing in properties.";
            throw new IllegalArgumentException(msg);
        }
    }

    public String getTypePropertyOrDefault() {
        String type = System.getProperty("type", "int");
        type = checkTypeProperty(type);
        return type;
    }

    //checks, if smth wrong - return int
    protected static String checkTypeProperty(String type) {
        if (!isSupportedType(type)) {
            type = "int";
            String msg = "Unsupported type in system properties: '" + type + "' so type was set to int." +
                    " Suppoerted types are: " + SUPPORTED_TYPES;
            logger.info(msg);
            logger.warn(msg); //not sure if really needed
        }
        return type;
    }


    protected static Number parseStringToType(String value, String type) {
        return switch (type.toLowerCase()) {
            case "byte" -> Byte.parseByte(value);
            case "double" -> Double.parseDouble(value);
            case "float" -> Float.parseFloat(value);
            case "int" -> Integer.parseInt(value);
            case "long" -> Long.parseLong(value);
            case "short" -> Short.parseShort(value);
            default -> {
                String msg = "Failed to convert value '" + value +  "' to type '" + type + " '?";
                throw new CriticalResourceException(List.of(msg));
            }
        };
    }

    protected static Number castToType(Number value, String type) {
        return switch (type.toLowerCase()) {
            case "byte" -> value.byteValue();
            case "short" -> value.shortValue();
            case "int" -> value.intValue();
            case "long" -> value.longValue();
            case "float" -> value.floatValue();
            case "double" -> value.doubleValue();
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        };
    }



    public static boolean isSupportedType(String type) {
        return SUPPORTED_TYPES.contains(type.toLowerCase());
    }


}

