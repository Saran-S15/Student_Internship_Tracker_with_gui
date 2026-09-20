import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Centralized JDBC Database Connection Factory.
 */
public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/internship_tracker?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "Saran@2007";

    static {
        try {
            // Explicitly load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("[ERROR] MySQL JDBC Driver not found. Please ensure mysql-connector-j.jar is in classpath.");
        }
    }

    /**
     * Obtains a new database connection.
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
