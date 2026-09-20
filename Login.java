import java.sql.SQLException;
import java.util.Scanner;

/**
 * Login class extends User and implements Authentication interface.
 */
public class Login extends User implements Authentication {
    private UserDAO userDAO;

    public Login() {
        this.userDAO = new UserDAO();
    }

    public Login(int userId, String username, String password, String role, String fullName) {
        super(userId, username, password, role, fullName);
        this.userDAO = new UserDAO();
    }

    @Override
    public boolean login(String username, String password) throws InvalidLoginException {
        try {
            User user = userDAO.authenticate(username, password);
            if (user != null) {
                this.userId = user.getUserId();
                this.username = user.getUsername();
                this.password = user.getPassword();
                this.role = user.getRole();
                this.fullName = user.getFullName();
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new InvalidLoginException("Database error during login: " + e.getMessage());
        }
    }

    /**
     * Authenticates credentials and returns the active specialized User object.
     */
    public User authenticateUser(String username, String password) throws InvalidLoginException, SQLException {
        return userDAO.authenticate(username, password);
    }

    @Override
    public void displayDashboard(Scanner scanner, StudentManager studentMgr, InternshipManager internshipMgr) {
        System.out.println("Generic user dashboard for: " + fullName);
    }
}
