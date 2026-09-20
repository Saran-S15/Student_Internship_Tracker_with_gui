import java.util.Scanner;

/**
 * Concrete User class representing the ADMIN role.
 * Manages students only (Add, View, Search, Update, Delete).
 */
public class AdminUser extends User {

    public AdminUser() {
        super();
        this.role = "ADMIN";
    }

    public AdminUser(int userId, String username, String password, String fullName) {
        super(userId, username, password, "ADMIN", fullName);
    }

    @Override
    public void displayDashboard(Scanner scanner, StudentManager studentMgr, InternshipManager internshipMgr) {
        boolean running = true;
        while (running) {
            System.out.println("\n========================================================");
            System.out.println("                   ADMIN DASHBOARD                      ");
            System.out.println("   Logged in as: " + fullName + " (" + username + ")");
            System.out.println("========================================================");
            System.out.println(" 1. Add Student");
            System.out.println(" 2. View All Students");
            System.out.println(" 3. Search Student");
            System.out.println(" 4. Update Student");
            System.out.println(" 5. Delete Student");
            System.out.println(" 6. Logout");
            System.out.println("========================================================");
            System.out.print("Enter your choice (1-6): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    studentMgr.addStudent(scanner);
                    break;
                case "2":
                    studentMgr.viewAllStudents();
                    break;
                case "3":
                    studentMgr.searchStudent(scanner);
                    break;
                case "4":
                    studentMgr.updateStudent(scanner);
                    break;
                case "5":
                    studentMgr.deleteStudent(scanner);
                    break;
                case "6":
                    System.out.println("\n[✓] Logged out from Admin Dashboard successfully.");
                    running = false;
                    break;
                default:
                    System.out.println("[!] Invalid choice. Please select an option between 1 and 6.");
            }
        }
    }
}
