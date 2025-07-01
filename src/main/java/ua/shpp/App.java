package ua.shpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* Main class of multiplication table app..... */
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void run(String propsFile) throws CriticalResourceException {
        new MultiTable(propsFile);
    }

    public static void main(String[] args) {
        try {
            run("multiplicationTable.properties");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}