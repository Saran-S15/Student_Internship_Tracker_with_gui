/**
 * Model representing a Student entity.
 */
public class Student {
    private String studentId;
    private String name;
    private String department;
    private int year;
    private double cgpa;
    private Integer userId;

    public Student() {
    }

    public Student(String studentId, String name, String department, int year, double cgpa) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.year = year;
        this.cgpa = cgpa;
    }

    public Student(String studentId, String name, String department, int year, double cgpa, Integer userId) {
        this(studentId, name, department, year, cgpa);
        this.userId = userId;
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return String.format("ID: %-8s | Name: %-20s | Dept: %-15s | Year: %d | CGPA: %.2f",
                studentId, name, department, year, cgpa);
    }
}
