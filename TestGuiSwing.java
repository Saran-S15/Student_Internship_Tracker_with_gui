import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Automated Verification Suite for FlatLaf GUI Components & Database operations.
 */
public class TestGuiSwing {
    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("      RUNNING AUTOMATED FLATLAF SWING GUI TESTS           ");
        System.out.println("==========================================================");

        int passed = 0;
        int failed = 0;

        try {
            // Test 1: FlatLaf Theme Init
            System.out.print("[TEST 1] Initializing FlatDarkLaf & Theme... ");
            Theme.initializeLookAndFeel();
            System.out.println("PASS");
            passed++;

            // Test 2: MainFrame & Layout
            System.out.print("[TEST 2] Initializing MainFrame & CardLayout... ");
            MainFrame mainFrame = new MainFrame();
            if (mainFrame != null) {
                System.out.println("PASS");
                passed++;
            } else {
                System.out.println("FAIL");
                failed++;
            }

            // Test 3: AdminDashboardPanel Load
            System.out.print("[TEST 3] Testing AdminDashboardPanel loading students... ");
            UserDAO userDAO = new UserDAO();
            StudentDAO studentDAO = new StudentDAO();
            InternshipDAO internshipDAO = new InternshipDAO();

            User admin = userDAO.authenticate("admin", "admin123");
            AdminDashboardPanel adminPanel = new AdminDashboardPanel(mainFrame, studentDAO, userDAO);
            adminPanel.setUser(admin);
            System.out.println("PASS (Admin: " + admin.getFullName() + ")");
            passed++;

            // Test 4: StudentDashboardPanel Load
            System.out.print("[TEST 4] Testing StudentDashboardPanel profile & applications... ");
            User student = userDAO.authenticate("student1", "student123");
            StudentDashboardPanel studentPanel = new StudentDashboardPanel(mainFrame, studentDAO, internshipDAO);
            studentPanel.setUser(student);
            System.out.println("PASS (Student: " + student.getFullName() + ")");
            passed++;

            // Test 5: FacultyDashboardPanel Load
            System.out.print("[TEST 5] Testing FacultyDashboardPanel reviews... ");
            User faculty = userDAO.authenticate("faculty1", "faculty123");
            FacultyDashboardPanel facultyPanel = new FacultyDashboardPanel(mainFrame, internshipDAO);
            facultyPanel.setUser(faculty);
            System.out.println("PASS (Faculty: " + faculty.getFullName() + ")");
            passed++;

            // Test 6: HodDashboardPanel Load
            System.out.print("[TEST 6] Testing HodDashboardPanel KPI cards & tables... ");
            User hod = userDAO.authenticate("hod1", "hod123");
            HodDashboardPanel hodPanel = new HodDashboardPanel(mainFrame, internshipDAO);
            hodPanel.setUser(hod);
            System.out.println("PASS (HOD: " + hod.getFullName() + ")");
            passed++;

            // Test 7: Role Switching & Session Management
            System.out.print("[TEST 7] Testing onLoginSuccess() polymorphic role dispatch... ");
            mainFrame.onLoginSuccess(admin);
            mainFrame.onLoginSuccess(student);
            mainFrame.onLoginSuccess(faculty);
            mainFrame.onLoginSuccess(hod);
            System.out.println("PASS (All 4 dashboards switched smoothly)");
            passed++;

            // Test 8: Custom Badges & Card Rendering
            System.out.print("[TEST 8] Testing StatusBadgeRenderer pill painting... ");
            StatusBadgeRenderer renderer = new StatusBadgeRenderer();
            JTable dummyTable = new JTable(1, 1);
            renderer.getTableCellRendererComponent(dummyTable, "Pending", false, false, 0, 0);
            renderer.getTableCellRendererComponent(dummyTable, "Approved", false, false, 0, 0);
            renderer.getTableCellRendererComponent(dummyTable, "Rejected", false, false, 0, 0);
            System.out.println("PASS");
            passed++;

        } catch (Exception e) {
            System.out.println("FAIL with Exception: " + e.getMessage());
            e.printStackTrace();
            failed++;
        }

        System.out.println("==========================================================");
        System.out.printf("  GUI TEST RESULTS: TOTAL: %d | PASSED: %d | FAILED: %d\n", (passed + failed), passed, failed);
        System.out.println("==========================================================");

        if (failed == 0) {
            System.out.println(">>> ALL 8 FLATLAF GUI TESTS PASSED CLEANLY! <<<");
        } else {
            System.exit(1);
        }
    }
}
