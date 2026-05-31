package hr.algebra.exceptions;

public class CannotPerformActionException extends RuntimeException {
    public CannotPerformActionException(String message) {
        super(message);
    }
    public CannotPerformActionException(){
        super("Cannot perform action");
    }
}
