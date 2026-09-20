import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Manager class coordinating Internship operations for Student, Faculty, and HOD.
 */
public class InternshipManager {
    private final InternshipDAO internshipDAO;
    private final StudentDAO studentDAO;
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public InternshipManager() {
        this.internshipDAO = new InternshipDAO();
        this.studentDAO = new StudentDAO();
    }

    public static String formatDate(LocalDate date) {
        return (date != null) ? date.format(DATE_FMT) : "N/A";
    }

    public InternshipDAO getInternshipDAO() {
        return internshipDAO;
    }

    // ==========================================================
    // STUDENT ACTIONS
    // ==========================================================

    /**
     * Views student profile for the logged in user.
     */
    public void viewProfile(User currentUser) {
        System.out.println("\n============================= [ MY PROFILE ] =============================");
        try {
            Student student = studentDAO.getStudentByUserId(currentUser.getUserId());
            if (student == null) {
                // Try finding by name
                List<Student> match = studentDAO.searchStudents(currentUser.getFullName());
                if (!match.isEmpty()) {
                    student = match.get(0);
                }
            }

            if (student != null) {
                System.out.println("+----------------------+------------------------------------------+");
                System.out.printf("| %-20s | %-40s |\n", "Student ID", student.getStudentId());
                System.out.printf("| %-20s | %-40s |\n", "Full Name", student.getName());
                System.out.printf("| %-20s | %-40s |\n", "Username", currentUser.getUsername());
                System.out.printf("| %-20s | %-40s |\n", "Department", student.getDepartment());
                System.out.printf("| %-20s | %-40d |\n", "Year of Study", student.getYear());
                System.out.printf("| %-20s | %-40.2f |\n", "CGPA", student.getCgpa());
                System.out.println("+----------------------+------------------------------------------+");
            } else {
                System.out.println("User: " + currentUser.getFullName() + " (" + currentUser.getUsername() + ")");
                System.out.println("Note: No student academic profile is linked to this login yet. Contact Admin.");
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to load profile: " + e.getMessage());
        }
    }

    /**
     * Submits a new internship application (Status defaults to Pending).
     */
    public void submitInternship(Scanner scanner, User currentUser) {
        System.out.println("\n----------------- [ SUBMIT INTERNSHIP APPLICATION ] -----------------");
        try {
            // Find student details
            Student student = studentDAO.getStudentByUserId(currentUser.getUserId());
            String studentId = (student != null) ? student.getStudentId() : "STU-" + currentUser.getUserId();
            String studentName = (student != null) ? student.getName() : currentUser.getFullName();

            System.out.println("Submitting application for: " + studentName + " (" + studentId + ")");

            System.out.print("Enter Company Name: ");
            String company = scanner.nextLine().trim();
            if (company.isEmpty()) {
                System.out.println("[!] Company Name cannot be empty.");
                return;
            }

            System.out.print("Enter Company Location (City/Country): ");
            String location = scanner.nextLine().trim();
            if (location.isEmpty()) {
                System.out.println("[!] Company Location cannot be empty.");
                return;
            }

            System.out.print("Enter Domain (e.g. Software, AI/ML, Cloud, Web): ");
            String domain = scanner.nextLine().trim();

            System.out.print("Enter Role/Designation (e.g. SDE Intern): ");
            String role = scanner.nextLine().trim();
            if (role.isEmpty()) {
                System.out.println("[!] Role cannot be empty.");
                return;
            }

            System.out.print("Enter Mode [1: Online | 2: Offline | 3: Hybrid]: ");
            String modeChoice = scanner.nextLine().trim();
            String mode = "Hybrid";
            if (modeChoice.equals("1") || modeChoice.equalsIgnoreCase("online")) {
                mode = "Online";
            } else if (modeChoice.equals("2") || modeChoice.equalsIgnoreCase("offline")) {
                mode = "Offline";
            } else if (modeChoice.equals("3") || modeChoice.equalsIgnoreCase("hybrid")) {
                mode = "Hybrid";
            }

            System.out.print("Enter Start Date (DD-MM-YYYY): ");
            String startStr = scanner.nextLine().trim();
            LocalDate startDate;
            try {
                startDate = LocalDate.parse(startStr, DATE_FMT);
            } catch (DateTimeParseException e) {
                System.out.println("[!] Invalid date format. Please use DD-MM-YYYY (e.g. 01-06-2026).");
                return;
            }

            System.out.print("Enter End Date (DD-MM-YYYY): ");
            String endStr = scanner.nextLine().trim();
            LocalDate endDate;
            try {
                endDate = LocalDate.parse(endStr, DATE_FMT);
            } catch (DateTimeParseException e) {
                System.out.println("[!] Invalid date format. Please use DD-MM-YYYY (e.g. 31-08-2026).");
                return;
            }

            if (endDate.isBefore(startDate)) {
                System.out.println("[!] Error: End date cannot be before Start date.");
                return;
            }

            Internship internship = new Internship(studentId, studentName, company, location, domain, role, mode, startDate, endDate);
            boolean success = internshipDAO.submitInternship(internship);
            if (success) {
                System.out.println("[✓] Internship submitted successfully! Current Status: Pending");
            } else {
                System.out.println("[!] Failed to submit internship.");
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Database error during submission: " + e.getMessage());
        }
    }

    /**
     * Views all internships submitted by the currently logged in student.
     */
    public void viewOwnInternships(User currentUser) {
        System.out.println("\n============================= [ MY INTERNSHIPS ] =============================");
        try {
            Student student = studentDAO.getStudentByUserId(currentUser.getUserId());
            String studentId = (student != null) ? student.getStudentId() : "STU-" + currentUser.getUserId();

            List<Internship> list = internshipDAO.getInternshipsByStudentId(studentId);
            if (list.isEmpty()) {
                System.out.println("You have not submitted any internship applications yet.");
                return;
            }
            printInternshipTable(list, true);
        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to fetch your internships: " + e.getMessage());
        }
    }

    // ==========================================================
    // FACULTY ACTIONS
    // ==========================================================

    /**
     * Reviews pending internships one-by-one with Approve/Reject/Skip options.
     */
    public void reviewPendingInternships(Scanner scanner) {
        System.out.println("\n======================== [ PENDING INTERNSHIP APPLICATIONS ] ========================");
        try {
            List<Internship> pendingList = internshipDAO.getInternshipsByStatus("Pending");
            if (pendingList.isEmpty()) {
                System.out.println("No pending internship applications to review.");
                return;
            }

            System.out.println("Found " + pendingList.size() + " pending application(s).\n");

            for (int i = 0; i < pendingList.size(); i++) {
                Internship item = pendingList.get(i);
                System.out.println("--------------------------------------------------------------------------------");
                System.out.printf("[%d/%d] Application ID : %d\n", (i + 1), pendingList.size(), item.getInternshipId());
                System.out.printf("      Student Name   : %s (%s)\n", item.getStudentName(), item.getStudentId());
                System.out.printf("      Company        : %s (%s)\n", item.getCompanyName(), item.getCompanyLocation());
                System.out.printf("      Domain & Role  : %s | %s\n", item.getDomain(), item.getRole());
                System.out.printf("      Mode & Duration: %s | %s to %s (%s)\n",
                        item.getInternshipMode(), formatDate(item.getStartDate()), formatDate(item.getEndDate()), item.getDurationString());
                System.out.println("--------------------------------------------------------------------------------");

                String action = "";
                while (!action.equals("A") && !action.equals("R") && !action.equals("S")) {
                    System.out.print("Action -> [A]pprove | [R]eject | [S]kip: ");
                    action = scanner.nextLine().trim().toUpperCase();
                    if (!action.equals("A") && !action.equals("R") && !action.equals("S")) {
                        System.out.println("[!] Invalid choice. Please enter A, R, or S.");
                    }
                }

                if (action.equals("A")) {
                    // Approve directly without prompting for remark
                    boolean ok = internshipDAO.updateStatusAndRemark(item.getInternshipId(), "Approved", "Approved by Faculty");
                    if (ok) {
                        System.out.println("[✓] Application ID " + item.getInternshipId() + " has been APPROVED.");
                    }
                } else if (action.equals("R")) {
                    System.out.print("Enter rejection remark (Required): ");
                    String remark = scanner.nextLine().trim();
                    while (remark.isEmpty()) {
                        System.out.print("[!] Rejection remark cannot be empty. Please enter reason: ");
                        remark = scanner.nextLine().trim();
                    }
                    boolean ok = internshipDAO.updateStatusAndRemark(item.getInternshipId(), "Rejected", remark);
                    if (ok) {
                        System.out.println("[✓] Application ID " + item.getInternshipId() + " has been REJECTED.");
                    }
                } else if (action.equals("S")) {
                    System.out.println("[*] Application skipped.");
                }
                System.out.println();
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Error during pending reviews: " + e.getMessage());
        }
    }

    /**
     * Views all Approved Internships.
     */
    public void viewApprovedInternships() {
        System.out.println("\n============================= [ APPROVED INTERNSHIPS ] =============================");
        try {
            List<Internship> list = internshipDAO.getInternshipsByStatus("Approved");
            if (list.isEmpty()) {
                System.out.println("No approved internship records found.");
                return;
            }
            printInternshipTable(list, true);
        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to fetch approved internships: " + e.getMessage());
        }
    }

    /**
     * Views all Rejected Internships with remarks.
     */
    public void viewRejectedInternships() {
        System.out.println("\n============================= [ REJECTED INTERNSHIPS ] =============================");
        try {
            List<Internship> list = internshipDAO.getInternshipsByStatus("Rejected");
            if (list.isEmpty()) {
                System.out.println("No rejected internship records found.");
                return;
            }
            printInternshipTable(list, true);
        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to fetch rejected internships: " + e.getMessage());
        }
    }

    // ==========================================================
    // HOD ACTIONS
    // ==========================================================

    /**
     * Displays overview summary cards (Total, Approved, Rejected, Pending counts).
     */
    public void viewOverview() {
        System.out.println("\n========================= [ HOD INTERNSHIP OVERVIEW ] =========================");
        try {
            Map<String, Integer> counts = internshipDAO.getOverviewCounts();
            int total = counts.getOrDefault("TOTAL", 0);
            int approved = counts.getOrDefault("APPROVED", 0);
            int rejected = counts.getOrDefault("REJECTED", 0);
            int pending = counts.getOrDefault("PENDING", 0);

            double approvalRate = (total > 0) ? ((double) approved / total) * 100 : 0.0;

            System.out.println("+-----------------------+-----------------------+");
            System.out.printf("| %-21s | %-21d |\n", "Total Applications", total);
            System.out.printf("| %-21s | %-21d |\n", "Approved Internships", approved);
            System.out.printf("| %-21s | %-21d |\n", "Rejected Internships", rejected);
            System.out.printf("| %-21s | %-21d |\n", "Pending Reviews", pending);
            System.out.printf("| %-21s | %-20.1f%% |\n", "Approval Rate", approvalRate);
            System.out.println("+-----------------------+-----------------------+");
        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to load overview counts: " + e.getMessage());
        }
    }

    /**
     * Displays all student internship statuses along with duration calculation.
     */
    public void viewStudentInternshipStatus() {
        System.out.println("\n======================== [ STUDENT INTERNSHIP STATUS & DURATION ] ========================");
        try {
            List<Internship> list = internshipDAO.getAllInternships();
            if (list.isEmpty()) {
                System.out.println("No internship records found in database.");
                return;
            }

            String line = "+----+------------+--------------------+------------------+------------------+---------------------+----------+";
            System.out.println(line);
            System.out.printf("| %-2s | %-10s | %-18s | %-16s | %-16s | %-19s | %-8s |\n",
                    "ID", "Student ID", "Student Name", "Company", "Role", "Duration", "Status");
            System.out.println(line);
            for (Internship in : list) {
                System.out.printf("| %-2d | %-10s | %-18s | %-16s | %-16s | %-19s | %-8s |\n",
                        in.getInternshipId(), in.getStudentId(), in.getStudentName(),
                        in.getCompanyName(), in.getRole(), in.getDurationString(), in.getStatus());
            }
            System.out.println(line);
        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to load status: " + e.getMessage());
        }
    }

    /**
     * Generates a detailed TXT report at D:\InternshipTracker\Internship_Report.txt.
     */
    public void generateTxtReport() {
        String reportPath = "D:\\InternshipTracker\\Internship_Report.txt";
        System.out.println("\nGenerating report to: " + reportPath + " ...");

        try (PrintWriter writer = new PrintWriter(new FileWriter(reportPath))) {
            Map<String, Integer> counts = internshipDAO.getOverviewCounts();
            List<Internship> allList = internshipDAO.getAllInternships();

            writer.println("========================================================================================");
            writer.println("                        STUDENT INTERNSHIP TRACKER - HOD REPORT                         ");
            writer.println("========================================================================================");
            writer.println("Generated On : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("Generated By : Head of Department (HOD)");
            writer.println();
            writer.println("----------------------------------------------------------------------------------------");
            writer.println("                                EXECUTIVE SUMMARY                                       ");
            writer.println("----------------------------------------------------------------------------------------");
            writer.printf("Total Applications     : %d\n", counts.getOrDefault("TOTAL", 0));
            writer.printf("Approved Applications  : %d\n", counts.getOrDefault("APPROVED", 0));
            writer.printf("Rejected Applications  : %d\n", counts.getOrDefault("REJECTED", 0));
            writer.printf("Pending Applications   : %d\n", counts.getOrDefault("PENDING", 0));
            int total = counts.getOrDefault("TOTAL", 0);
            double rate = (total > 0) ? ((double) counts.getOrDefault("APPROVED", 0) / total) * 100 : 0.0;
            writer.printf("Overall Approval Rate  : %.2f%%\n", rate);
            writer.println();

            writer.println("----------------------------------------------------------------------------------------");
            writer.println("                            DETAILED INTERNSHIP RECORDS                                 ");
            writer.println("----------------------------------------------------------------------------------------");
            String line = "+-----+------------+--------------------+--------------------+--------------------+------------+------------+----------+-------------------------------------+";
            writer.println(line);
            writer.printf("| %-3s | %-10s | %-18s | %-18s | %-18s | %-10s | %-10s | %-8s | %-35s |\n",
                    "ID", "Student ID", "Student Name", "Company", "Role", "Start Date", "End Date", "Status", "Remark");
            writer.println(line);

            for (Internship in : allList) {
                String remarkStr = (in.getRemark() != null) ? in.getRemark() : "-";
                if (remarkStr.length() > 35) {
                    remarkStr = remarkStr.substring(0, 32) + "...";
                }
                writer.printf("| %-3d | %-10s | %-18s | %-18s | %-18s | %-10s | %-10s | %-8s | %-35s |\n",
                        in.getInternshipId(), in.getStudentId(), in.getStudentName(),
                        in.getCompanyName(), in.getRole(),
                        formatDate(in.getStartDate()), formatDate(in.getEndDate()),
                        in.getStatus(), remarkStr);
            }
            writer.println(line);
            writer.println();
            writer.println("================================== END OF REPORT =======================================");

            System.out.println("[✓] Report successfully generated!");
            System.out.println("    File Location: " + reportPath);
            System.out.println("    Total Records Exported: " + allList.size());

        } catch (IOException e) {
            System.out.println("[ERROR] Failed to write report file: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to fetch DB records for report: " + e.getMessage());
        }
    }

    public static void printInternshipTable(List<Internship> list, boolean showRemarks) {
        String line = "+----+------------+--------------------+--------------------+--------------------+---------+------------+------------+----------+-------------------------------------+";
        System.out.println(line);
        System.out.printf("| %-2s | %-10s | %-18s | %-18s | %-18s | %-7s | %-10s | %-10s | %-8s | %-35s |\n",
                "ID", "Student ID", "Student Name", "Company", "Role", "Mode", "Start Date", "End Date", "Status", "Remark");
        System.out.println(line);
        for (Internship in : list) {
            String remarkStr = (in.getRemark() != null) ? in.getRemark() : "-";
            if (remarkStr.length() > 35) {
                remarkStr = remarkStr.substring(0, 32) + "...";
            }
            System.out.printf("| %-2d | %-10s | %-18s | %-18s | %-18s | %-7s | %-10s | %-10s | %-8s | %-35s |\n",
                    in.getInternshipId(), in.getStudentId(), in.getStudentName(),
                    in.getCompanyName(), in.getRole(), in.getInternshipMode(),
                    formatDate(in.getStartDate()), formatDate(in.getEndDate()), in.getStatus(), remarkStr);
        }
        System.out.println(line);
    }
}
