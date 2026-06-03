package hr.algebra.exceptions;

public class InvalidParseException extends RuntimeException {
    public InvalidParseException(String message) {
        super(message);
    }
    public InvalidParseException() {
        super("Invalid parse");
    }
}
