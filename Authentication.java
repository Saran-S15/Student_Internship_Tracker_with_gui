/**
 * Interface defining authentication contract.
 */
public interface Authentication {
    boolean login(String username, String password) throws InvalidLoginException;
}
