import java.util.Scanner;

/**
 * Concrete User class representing the STUDENT role.
 */
public class StudentUser extends User {

    public StudentUser() {
        super();
        this.role = "STUDENT";
    }

    public StudentUser(int userId, String username, String password, String fullName) {
        super(userId, username, password, "STUDENT", fullName);
    }

    @Override
    public void displayDashboard(Scanner scanner, StudentManager studentMgr, InternshipManager internshipMgr) {
        boolean running = true;
        while (running) {
            System.out.println("\n========================================================");
            System.out.println("                  STUDENT DASHBOARD                     ");
            System.out.println("   Welcome, " + fullName + " (" + username + ")");
            System.out.println("========================================================");
            System.out.println(" 1. View Profile");
            System.out.println(" 2. Submit Internship Application");
            System.out.println(" 3. View My Internships");
            System.out.println(" 4. Logout");
            System.out.println("========================================================");
            System.out.print("Enter your choice (1-4): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    internshipMgr.viewProfile(this);
                    break;
                case "2":
                    internshipMgr.submitInternship(scanner, this);
                    break;
                case "3":
                    internshipMgr.viewOwnInternships(this);
                    break;
                case "4":
                    System.out.println("\n[✓] Logged out from Student Dashboard successfully.");
                    running = false;
                    break;
                default:
                    System.out.println("[!] Invalid choice. Please select an option between 1 and 4.");
            }
        }
    }
}
