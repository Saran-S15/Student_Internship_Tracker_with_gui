import java.util.Scanner;

/**
 * Abstract Base Class for all User roles in the system.
 */
public abstract class User {
    protected int userId;
    protected String username;
    protected String password;
    protected String role; // ADMIN, STUDENT, FACULTY, HOD
    protected String fullName;

    public User() {
    }

    public User(int userId, String username, String password, String role, String fullName) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Polymorphic dashboard method implemented by each role subclass.
     */
    public abstract void displayDashboard(Scanner scanner, StudentManager studentMgr, InternshipManager internshipMgr);
}
