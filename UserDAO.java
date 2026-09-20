import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object for User Authentication and Queries.
 */
public class UserDAO {

    /**
     * Authenticates a user against the database and returns the corresponding polymorphic User object.
     */
    public User authenticate(String username, String password) throws SQLException, InvalidLoginException {
        String sql = "SELECT user_id, username, password, role, full_name FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("user_id");
                    String uname = rs.getString("username");
                    String pass = rs.getString("password");
                    String role = rs.getString("role");
                    String name = rs.getString("full_name");

                    return createUserInstance(id, uname, pass, role, name);
                } else {
                    throw new InvalidLoginException("Invalid username or password.");
                }
            }
        }
    }

    /**
     * Finds a user by their username.
     */
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT user_id, username, password, role, full_name FROM users WHERE username = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("user_id");
                    String uname = rs.getString("username");
                    String pass = rs.getString("password");
                    String role = rs.getString("role");
                    String name = rs.getString("full_name");

                    return createUserInstance(id, uname, pass, role, name);
                }
            }
        }
        return null;
    }

    /**
     * Creates a new user record in the users table.
     */
    public int createUser(String username, String password, String role, String fullName) throws SQLException {
        String sql = "INSERT INTO users (username, password, role, full_name) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setString(4, fullName);
            
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1);
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Checks if a username already exists in the users table.
     */
    public boolean isUsernameTaken(String username) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    /**
     * Deletes a user by their user_id.
     */
    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a user by their username.
     */
    public boolean deleteUserByUsername(String username) throws SQLException {
        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Helper factory to instantiate the specific User subclass based on role.
     */
    public static User createUserInstance(int id, String uname, String pass, String role, String name) {
        if (role == null) return null;
        switch (role.trim().toUpperCase()) {
            case "ADMIN":
                return new AdminUser(id, uname, pass, name);
            case "STUDENT":
                return new StudentUser(id, uname, pass, name);
            case "FACULTY":
                return new FacultyUser(id, uname, pass, name);
            case "HOD":
                return new HodUser(id, uname, pass, name);
            default:
                return new Login(id, uname, pass, role, name);
        }
    }
}
