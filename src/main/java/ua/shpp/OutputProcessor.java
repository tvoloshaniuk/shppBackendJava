package ua.shpp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
public class OutputProcessor {
    private static final Logger logger = LoggerFactory.getLogger(OutputProcessor.class);

    /* Outputs a message in the specified format (JSON or XML). */
    public static void printMessage(Message message, String outputFormat) throws JsonProcessingException {
        logger.trace("TRACE: Entered printMessage method.");
        logger.debug("DEBUG: Preparing to print message in {} format.", outputFormat);

        logger.info("outputFormat, which came into printMessage as param: {}", outputFormat);

        ObjectMapper mapper = "xml".equalsIgnoreCase(outputFormat) ? new XmlMapper() : new ObjectMapper();
        logger.info(mapper.writeValueAsString(message));

//        try {
//
//        } catch (JsonProcessingException e) {
//            logger.error("Error generating output: {}", e.getMessage(), e);
//            System.exit(1);
//        }
    }


}
