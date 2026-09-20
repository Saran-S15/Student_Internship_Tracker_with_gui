import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Manager class handling Student business logic and operations for Admin.
 */
public class StudentManager {
    private final StudentDAO studentDAO;
    private final UserDAO userDAO;

    public StudentManager() {
        this.studentDAO = new StudentDAO();
        this.userDAO = new UserDAO();
    }

    public StudentDAO getStudentDAO() {
        return studentDAO;
    }

    /**
     * Handles adding a new student.
     */
    public void addStudent(Scanner scanner) {
        System.out.println("\n----------------- [ ADD NEW STUDENT ] -----------------");
        try {
            System.out.print("Enter Student ID (e.g. STU104): ");
            String studentId = scanner.nextLine().trim();
            if (studentId.isEmpty()) {
                System.out.println("[!] Student ID cannot be empty.");
                return;
            }

            if (studentDAO.getStudentById(studentId) != null) {
                System.out.println("[!] Error: Student ID '" + studentId + "' already exists!");
                return;
            }

            System.out.print("Enter Full Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("[!] Name cannot be empty.");
                return;
            }

            System.out.print("Enter Department (e.g. Computer Science): ");
            String dept = scanner.nextLine().trim();

            System.out.print("Enter Year of Study (1-4): ");
            int year;
            try {
                year = Integer.parseInt(scanner.nextLine().trim());
                if (year < 1 || year > 4) {
                    System.out.println("[!] Invalid year. Must be between 1 and 4.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid number format for year.");
                return;
            }

            System.out.print("Enter CGPA (0.0 - 10.0): ");
            double cgpa;
            try {
                cgpa = Double.parseDouble(scanner.nextLine().trim());
                if (cgpa < 0.0 || cgpa > 10.0) {
                    System.out.println("[!] Invalid CGPA. Must be between 0.0 and 10.0.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Invalid number format for CGPA.");
                return;
            }

            System.out.print("Create a login user account for this student? (y/n): ");
            String createLogin = scanner.nextLine().trim();
            Integer userId = null;
            if (createLogin.equalsIgnoreCase("y")) {
                System.out.print("Enter username (default: " + studentId.toLowerCase() + "): ");
                String uname = scanner.nextLine().trim();
                if (uname.isEmpty()) uname = studentId.toLowerCase();

                // Check if username is already taken
                while (userDAO.isUsernameTaken(uname)) {
                    System.out.print("[!] Username '" + uname + "' already exists! Please enter a different username: ");
                    uname = scanner.nextLine().trim();
                    if (uname.isEmpty()) uname = studentId.toLowerCase() + "_user";
                }

                System.out.print("Enter password (default: student123): ");
                String pwd = scanner.nextLine().trim();
                if (pwd.isEmpty()) pwd = "student123";

                userId = userDAO.createUser(uname, pwd, "STUDENT", name);
                System.out.println("[+] User account created with username: " + uname);
            }

            Student student = new Student(studentId, name, dept, year, cgpa, userId);
            boolean success = studentDAO.addStudent(student);
            if (success) {
                System.out.println("[✓] Student added successfully to database!");
            } else {
                System.out.println("[!] Failed to add student.");
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Database error while adding student: " + e.getMessage());
        } catch (StudentException e) {
            System.out.println("[VALIDATION ERROR] " + e.getMessage());
        }
    }

    /**
     * Displays all students in a formatted ASCII table.
     */
    public void viewAllStudents() {
        System.out.println("\n=================================== [ ALL STUDENTS ] ===================================");
        try {
            List<Student> list = studentDAO.getAllStudents();
            if (list.isEmpty()) {
                System.out.println("No student records found.");
                return;
            }
            printStudentTable(list);
        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to fetch students: " + e.getMessage());
        }
    }

    /**
     * Searches students by ID or Name.
     */
    public void searchStudent(Scanner scanner) {
        System.out.println("\n----------------- [ SEARCH STUDENT ] -----------------");
        System.out.print("Enter search keyword (Student ID, Name, or Department): ");
        String keyword = scanner.nextLine().trim();
        if (keyword.isEmpty()) {
            System.out.println("[!] Search keyword cannot be empty.");
            return;
        }

        try {
            List<Student> results = studentDAO.searchStudents(keyword);
            if (results.isEmpty()) {
                System.out.println("No matching student records found for: " + keyword);
            } else {
                System.out.println("\nFound " + results.size() + " matching record(s):");
                printStudentTable(results);
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Search failed: " + e.getMessage());
        }
    }

    /**
     * Updates an existing student record.
     */
    public void updateStudent(Scanner scanner) {
        System.out.println("\n----------------- [ UPDATE STUDENT ] -----------------");
        System.out.print("Enter Student ID to update: ");
        String studentId = scanner.nextLine().trim();

        try {
            Student existing = studentDAO.getStudentById(studentId);
            if (existing == null) {
                System.out.println("[!] Student with ID '" + studentId + "' not found.");
                return;
            }

            System.out.println("\nCurrent Details: " + existing);
            System.out.println("(Press ENTER without typing to keep current value)");

            System.out.print("New Name [" + existing.getName() + "]: ");
            String newName = scanner.nextLine().trim();
            if (!newName.isEmpty()) existing.setName(newName);

            System.out.print("New Department [" + existing.getDepartment() + "]: ");
            String newDept = scanner.nextLine().trim();
            if (!newDept.isEmpty()) existing.setDepartment(newDept);

            System.out.print("New Year [" + existing.getYear() + "]: ");
            String yearInput = scanner.nextLine().trim();
            if (!yearInput.isEmpty()) {
                try {
                    int yr = Integer.parseInt(yearInput);
                    if (yr >= 1 && yr <= 4) {
                        existing.setYear(yr);
                    } else {
                        System.out.println("[!] Invalid year entered; keeping existing year.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[!] Invalid number format; keeping existing year.");
                }
            }

            System.out.print("New CGPA [" + existing.getCgpa() + "]: ");
            String cgpaInput = scanner.nextLine().trim();
            if (!cgpaInput.isEmpty()) {
                try {
                    double gpa = Double.parseDouble(cgpaInput);
                    if (gpa >= 0.0 && gpa <= 10.0) {
                        existing.setCgpa(gpa);
                    } else {
                        System.out.println("[!] Invalid CGPA; keeping existing CGPA.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("[!] Invalid number format; keeping existing CGPA.");
                }
            }

            boolean updated = studentDAO.updateStudent(existing);
            if (updated) {
                System.out.println("[✓] Student record updated successfully in database!");
            } else {
                System.out.println("[!] Failed to update student record.");
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Database error during update: " + e.getMessage());
        } catch (StudentException e) {
            System.out.println("[VALIDATION ERROR] " + e.getMessage());
        }
    }

    /**
     * Deletes a student record after confirmation.
     */
    public void deleteStudent(Scanner scanner) {
        System.out.println("\n----------------- [ DELETE STUDENT ] -----------------");
        System.out.print("Enter Student ID to delete: ");
        String studentId = scanner.nextLine().trim();

        try {
            Student student = studentDAO.getStudentById(studentId);
            if (student == null) {
                System.out.println("[!] Student with ID '" + studentId + "' not found.");
                return;
            }

            System.out.println("Record to delete: " + student);
            System.out.print("Are you sure you want to permanently delete this student? (yes/no): ");
            String confirm = scanner.nextLine().trim();

            if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
                boolean deleted = studentDAO.deleteStudent(studentId);
                if (deleted) {
                    System.out.println("[✓] Student deleted successfully from database!");
                } else {
                    System.out.println("[!] Failed to delete student.");
                }
            } else {
                System.out.println("[*] Deletion canceled.");
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Database error during delete: " + e.getMessage());
        } catch (StudentException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
    }

    public static void printStudentTable(List<Student> list) {
        String line = "+------------+----------------------+-----------------------+------+-------+";
        System.out.println(line);
        System.out.printf("| %-10s | %-20s | %-21s | %-4s | %-5s |\n", "Student ID", "Full Name", "Department", "Year", "CGPA");
        System.out.println(line);
        for (Student s : list) {
            System.out.printf("| %-10s | %-20s | %-21s | %-4d | %-5.2f |\n",
                    s.getStudentId(), s.getName(), s.getDepartment(), s.getYear(), s.getCgpa());
        }
        System.out.println(line);
    }
}
