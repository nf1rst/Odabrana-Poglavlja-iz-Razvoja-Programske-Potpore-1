package hr.algebra.exceptions;

public class InitializationException extends RuntimeException {
    public InitializationException(String message, Throwable cause) {
        super(message, cause);
    }
    public InitializationException() {
        super("Initialiation not successful");
    }
    public InitializationException(String message) {
        super(message);
    }
}
