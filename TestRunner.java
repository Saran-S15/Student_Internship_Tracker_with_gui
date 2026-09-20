import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Automated Test Runner to verify end-to-end database operations and business logic.
 */
public class TestRunner {
    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("  STARTING AUTOMATED INTEGRATION TESTS (JDBC + MySQL)     ");
        System.out.println("==========================================================");

        int passed = 0;
        int failed = 0;

        UserDAO userDAO = new UserDAO();
        StudentDAO studentDAO = new StudentDAO();
        InternshipDAO internshipDAO = new InternshipDAO();
        InternshipManager internshipMgr = new InternshipManager();

        // 1. Test Authentication for all 4 roles
        try {
            System.out.print("[TEST 1] Authenticating ADMIN (admin/admin123)... ");
            User admin = userDAO.authenticate("admin", "admin123");
            if (admin instanceof AdminUser && admin.getRole().equals("ADMIN")) {
                System.out.println("PASS (" + admin.getFullName() + ")");
                passed++;
            } else {
                System.out.println("FAIL");
                failed++;
            }

            System.out.print("[TEST 2] Authenticating STUDENT (student1/student123)... ");
            User stu = userDAO.authenticate("student1", "student123");
            if (stu instanceof StudentUser && stu.getRole().equals("STUDENT")) {
                System.out.println("PASS (" + stu.getFullName() + ")");
                passed++;
            } else {
                System.out.println("FAIL");
                failed++;
            }

            System.out.print("[TEST 3] Authenticating FACULTY (faculty1/faculty123)... ");
            User fac = userDAO.authenticate("faculty1", "faculty123");
            if (fac instanceof FacultyUser && fac.getRole().equals("FACULTY")) {
                System.out.println("PASS (" + fac.getFullName() + ")");
                passed++;
            } else {
                System.out.println("FAIL");
                failed++;
            }

            System.out.print("[TEST 4] Authenticating HOD (hod1/hod123)... ");
            User hod = userDAO.authenticate("hod1", "hod123");
            if (hod instanceof HodUser && hod.getRole().equals("HOD")) {
                System.out.println("PASS (" + hod.getFullName() + ")");
                passed++;
            } else {
                System.out.println("FAIL");
                failed++;
            }

            System.out.print("[TEST 5] Invalid Credentials check (admin/wrongpass)... ");
            try {
                userDAO.authenticate("admin", "wrongpass");
                System.out.println("FAIL (Should have thrown InvalidLoginException)");
                failed++;
            } catch (InvalidLoginException e) {
                System.out.println("PASS (Caught expected: " + e.getMessage() + ")");
                passed++;
            }

        } catch (Exception e) {
            System.out.println("FAIL: " + e.getMessage());
            failed++;
        }

        // 2. Test Student CRUD
        try {
            System.out.print("[TEST 6] Student CRUD: Add new student STU999... ");
            Student s = new Student("STU999", "Test Candidate", "CSE", 2, 9.1);
            // Clean up first if exists
            if (studentDAO.getStudentById("STU999") != null) {
                studentDAO.deleteStudent("STU999");
            }
            boolean added = studentDAO.addStudent(s);
            if (added) {
                System.out.println("PASS");
                passed++;
            } else {
                System.out.println("FAIL");
                failed++;
            }

            System.out.print("[TEST 7] Student CRUD: Fetch and Verify STU999... ");
            Student fetched = studentDAO.getStudentById("STU999");
            if (fetched != null && fetched.getName().equals("Test Candidate") && fetched.getYear() == 2) {
                System.out.println("PASS");
                passed++;
            } else {
                System.out.println("FAIL");
                failed++;
            }

            System.out.print("[TEST 8] Student CRUD: Update STU999... ");
            fetched.setName("Updated Candidate");
            fetched.setCgpa(9.5);
            boolean updated = studentDAO.updateStudent(fetched);
            Student refetched = studentDAO.getStudentById("STU999");
            if (updated && refetched != null && refetched.getName().equals("Updated Candidate") && refetched.getCgpa() == 9.5) {
                System.out.println("PASS");
                passed++;
            } else {
                System.out.println("FAIL");
                failed++;
            }

            System.out.print("[TEST 9] Student CRUD: Delete STU999... ");
            boolean deleted = studentDAO.deleteStudent("STU999");
            Student checkDeleted = studentDAO.getStudentById("STU999");
            if (deleted && checkDeleted == null) {
                System.out.println("PASS");
                passed++;
            } else {
                System.out.println("FAIL");
                failed++;
            }

        } catch (Exception e) {
            System.out.println("FAIL: " + e.getMessage());
            failed++;
        }

        // 3. Test Internship Workflow
        try {
            System.out.print("[TEST 10] Submit Internship for STU101... ");
            Internship in = new Internship("STU101", "Alice Smith", "Meta Platforms", "Menlo Park",
                    "AI Systems", "Research Intern", "Hybrid",
                    LocalDate.now().plusDays(10), LocalDate.now().plusDays(100));
            boolean sub = internshipDAO.submitInternship(in);
            if (sub) {
                System.out.println("PASS");
                passed++;
            } else {
                System.out.println("FAIL");
                failed++;
            }

            System.out.print("[TEST 11] Verify Pending submissions for STU101... ");
            List<Internship> stuInternships = internshipDAO.getInternshipsByStudentId("STU101");
            Internship pendingMeta = null;
            for (Internship i : stuInternships) {
                if (i.getCompanyName().equals("Meta Platforms")) {
                    pendingMeta = i;
                    break;
                }
            }
            if (pendingMeta != null && pendingMeta.getStatus().equals("Pending")) {
                System.out.println("PASS (Internship ID: " + pendingMeta.getInternshipId() + ")");
                passed++;
            } else {
                System.out.println("FAIL");
                failed++;
            }

            System.out.print("[TEST 12] Faculty Approval workflow on Meta Platforms... ");
            if (pendingMeta != null) {
                boolean approved = internshipDAO.updateStatusAndRemark(pendingMeta.getInternshipId(), "Approved", "Approved by Faculty Test");
                Internship updatedIn = internshipDAO.getInternshipById(pendingMeta.getInternshipId());
                if (approved && updatedIn != null && updatedIn.getStatus().equals("Approved")) {
                    System.out.println("PASS");
                    passed++;
                } else {
                    System.out.println("FAIL");
                    failed++;
                }
            }

            System.out.print("[TEST 13] HOD Overview Counts calculation... ");
            Map<String, Integer> counts = internshipDAO.getOverviewCounts();
            if (counts.containsKey("TOTAL") && counts.containsKey("APPROVED") && counts.containsKey("REJECTED") && counts.containsKey("PENDING")) {
                System.out.println("PASS (Total: " + counts.get("TOTAL") + ", Approved: " + counts.get("APPROVED") +
                        ", Rejected: " + counts.get("REJECTED") + ", Pending: " + counts.get("PENDING") + ")");
                passed++;
            } else {
                System.out.println("FAIL");
                failed++;
            }

            System.out.print("[TEST 14] HOD Generate TXT Report file... ");
            internshipMgr.generateTxtReport();
            File reportFile = new File("D:\\InternshipTracker\\Internship_Report.txt");
            if (reportFile.exists() && reportFile.length() > 0) {
                List<String> lines = Files.readAllLines(reportFile.toPath());
                System.out.println("PASS (Report Generated with " + lines.size() + " lines)");
                passed++;
            } else {
                System.out.println("FAIL");
                failed++;
            }

        } catch (Exception e) {
            System.out.println("FAIL: " + e.getMessage());
            failed++;
        }

        System.out.println("==========================================================");
        System.out.printf("  TEST RESULTS: TOTAL: %d | PASSED: %d | FAILED: %d\n", (passed + failed), passed, failed);
        System.out.println("==========================================================");

        if (failed == 0) {
            System.out.println(">>> ALL 14 AUTOMATED INTEGRATION TESTS PASSED SUCCESSFULLY! <<<");
        } else {
            System.exit(1);
        }
    }
}
