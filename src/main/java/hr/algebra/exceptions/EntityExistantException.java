package hr.algebra.exceptions;

public class EntityExistantException extends RuntimeException {
    public EntityExistantException(String message) {
        super(message);
    }
    public EntityExistantException(){
        super("Entity already exists");
    }
}
