package ua.shpp;

public class CriticalResourceException extends RuntimeException {
    public CriticalResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
