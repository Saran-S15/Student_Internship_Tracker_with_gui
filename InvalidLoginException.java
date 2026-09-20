/**
 * Custom Exception thrown when authentication fails.
 */
public class InvalidLoginException extends Exception {
    public InvalidLoginException(String message) {
        super(message);
    }
}
