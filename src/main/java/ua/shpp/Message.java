package ua.shpp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
//pojo
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//@JsonInclude.Include.NON_NULL //todo json include
public class Message {
    private String message;

    public String getMessage() {
        return message;
    }

    public Message setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        String sb = "Message{" + "message='" + message + '\'' +
                '}';
        return sb;
    }

}
