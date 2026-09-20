import java.util.Scanner;

/**
 * Concrete User class representing the FACULTY role.
 */
public class FacultyUser extends User {

    public FacultyUser() {
        super();
        this.role = "FACULTY";
    }

    public FacultyUser(int userId, String username, String password, String fullName) {
        super(userId, username, password, "FACULTY", fullName);
    }

    @Override
    public void displayDashboard(Scanner scanner, StudentManager studentMgr, InternshipManager internshipMgr) {
        boolean running = true;
        while (running) {
            System.out.println("\n========================================================");
            System.out.println("                  FACULTY DASHBOARD                     ");
            System.out.println("   Logged in as: " + fullName + " (" + username + ")");
            System.out.println("========================================================");
            System.out.println(" 1. View & Review Pending Internships (Approve/Reject)");
            System.out.println(" 2. View Approved Internships");
            System.out.println(" 3. View Rejected Internships");
            System.out.println(" 4. Logout");
            System.out.println("========================================================");
            System.out.print("Enter your choice (1-4): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    internshipMgr.reviewPendingInternships(scanner);
                    break;
                case "2":
                    internshipMgr.viewApprovedInternships();
                    break;
                case "3":
                    internshipMgr.viewRejectedInternships();
                    break;
                case "4":
                    System.out.println("\n[✓] Logged out from Faculty Dashboard successfully.");
                    running = false;
                    break;
                default:
                    System.out.println("[!] Invalid choice. Please select an option between 1 and 4.");
            }
        }
    }
}
