import java.util.Scanner;

/**
 * Concrete User class representing the HEAD OF DEPARTMENT (HOD) role.
 */
public class HodUser extends User {

    public HodUser() {
        super();
        this.role = "HOD";
    }

    public HodUser(int userId, String username, String password, String fullName) {
        super(userId, username, password, "HOD", fullName);
    }

    @Override
    public void displayDashboard(Scanner scanner, StudentManager studentMgr, InternshipManager internshipMgr) {
        boolean running = true;
        while (running) {
            System.out.println("\n========================================================");
            System.out.println("                     HOD DASHBOARD                      ");
            System.out.println("   Logged in as: " + fullName + " (" + username + ")");
            System.out.println("========================================================");
            System.out.println(" 1. Internship Overview (Counts & Statistics)");
            System.out.println(" 2. View Approved Internship Records");
            System.out.println(" 3. View Rejected Internship Records (with Remarks)");
            System.out.println(" 4. Student Internship Status & Duration");
            System.out.println(" 5. Generate TXT Report (Internship_Report.txt)");
            System.out.println(" 6. Logout");
            System.out.println("========================================================");
            System.out.print("Enter your choice (1-6): ");

            String input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    internshipMgr.viewOverview();
                    break;
                case "2":
                    internshipMgr.viewApprovedInternships();
                    break;
                case "3":
                    internshipMgr.viewRejectedInternships();
                    break;
                case "4":
                    internshipMgr.viewStudentInternshipStatus();
                    break;
                case "5":
                    internshipMgr.generateTxtReport();
                    break;
                case "6":
                    System.out.println("\n[✓] Logged out from HOD Dashboard successfully.");
                    running = false;
                    break;
                default:
                    System.out.println("[!] Invalid choice. Please select an option between 1 and 6.");
            }
        }
    }
}
