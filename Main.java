import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Main application entry point for the Student Internship Tracker.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentManager studentManager = new StudentManager();
        InternshipManager internshipManager = new InternshipManager();
        Login loginAuth = new Login();

        printWelcomeBanner();
        checkDatabaseConnectivity();

        boolean appRunning = true;
        while (appRunning) {
            System.out.println("\n========================================================");
            System.out.println("                   MAIN MENU                            ");
            System.out.println("========================================================");
            System.out.println(" 1. Login to Account");
            System.out.println(" 2. Exit Application");
            System.out.println("========================================================");
            System.out.print("Enter choice (1-2): ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    handleLoginFlow(scanner, loginAuth, studentManager, internshipManager);
                    break;
                case "2":
                    System.out.println("\nThank you for using Student Internship Tracker. Goodbye!");
                    appRunning = false;
                    break;
                default:
                    System.out.println("[!] Invalid choice. Please enter 1 or 2.");
            }
        }
        scanner.close();
    }

    private static void handleLoginFlow(Scanner scanner, Login loginAuth,
                                        StudentManager studentMgr, InternshipManager internshipMgr) {
        System.out.println("\n-------------------- [ SYSTEM LOGIN ] --------------------");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("[!] Username and password cannot be empty.");
            return;
        }

        try {
            User loggedInUser = loginAuth.authenticateUser(username, password);
            if (loggedInUser != null) {
                System.out.println("\n[✓] Authentication successful! Welcome, " + loggedInUser.getFullName() +
                                   " [" + loggedInUser.getRole() + "]");
                // Polymorphic invocation of role dashboard
                loggedInUser.displayDashboard(scanner, studentMgr, internshipMgr);
            }
        } catch (InvalidLoginException e) {
            System.out.println("\n[AUTHENTICATION FAILED] " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("\n[DATABASE ERROR] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n[SYSTEM ERROR] An unexpected error occurred: " + e.getMessage());
        }
    }

    private static void checkDatabaseConnectivity() {
        System.out.print("[...] Verifying MySQL connection... ");
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("[CONNECTED to MySQL: internship_tracker]");
            }
        } catch (SQLException e) {
            System.out.println("\n[WARNING] Could not connect to MySQL: " + e.getMessage());
            System.out.println("Please ensure MySQL is running and credentials in DBConnection.java are correct.\n");
        }
    }

    private static void printWelcomeBanner() {
        System.out.println("================================================================================");
        System.out.println("                          STUDENT INTERNSHIP TRACKER                            ");
        System.out.println("================================================================================");
    }
}
