package hr.algebra.exceptions;

public class DBConnectionException extends RuntimeException {
    public DBConnectionException(String message) {
        super(message);
    }
    public DBConnectionException(){
        super("Cannot connect to database");
    }
}
