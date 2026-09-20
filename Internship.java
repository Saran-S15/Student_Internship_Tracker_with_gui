import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Model representing an Internship application and review record.
 */
public class Internship {
    private int internshipId;
    private String studentId;
    private String studentName;
    private String companyName;
    private String companyLocation;
    private String domain;
    private String role;
    private String internshipMode; // Online, Offline, Hybrid
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // Pending, Approved, Rejected
    private String remark;

    public Internship() {
    }

    public Internship(int internshipId, String studentId, String studentName, String companyName,
                      String companyLocation, String domain, String role, String internshipMode,
                      LocalDate startDate, LocalDate endDate, String status, String remark) {
        this.internshipId = internshipId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.companyName = companyName;
        this.companyLocation = companyLocation;
        this.domain = domain;
        this.role = role;
        this.internshipMode = internshipMode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.remark = remark;
    }

    public Internship(String studentId, String studentName, String companyName,
                      String companyLocation, String domain, String role, String internshipMode,
                      LocalDate startDate, LocalDate endDate) {
        this(0, studentId, studentName, companyName, companyLocation, domain, role, internshipMode, startDate, endDate, "Pending", null);
    }

    // Getters and Setters
    public int getInternshipId() {
        return internshipId;
    }

    public void setInternshipId(int internshipId) {
        this.internshipId = internshipId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getInternshipMode() {
        return internshipMode;
    }

    public void setInternshipMode(String internshipMode) {
        this.internshipMode = internshipMode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Calculates duration between start date and end date.
     * @return Formatted duration string e.g. "90 days (~3 months)"
     */
    public String getDurationString() {
        if (startDate == null || endDate == null) {
            return "N/A";
        }
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        long months = ChronoUnit.MONTHS.between(startDate, endDate);
        if (months > 0) {
            return days + " days (~" + months + " mo)";
        }
        return days + " days";
    }

    private static final java.time.format.DateTimeFormatter DATE_FMT = java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public String toString() {
        String sDate = (startDate != null) ? startDate.format(DATE_FMT) : "N/A";
        String eDate = (endDate != null) ? endDate.format(DATE_FMT) : "N/A";
        return String.format("ID: %-4d | Student: %-18s | Company: %-16s | Role: %-15s | Mode: %-7s | %s to %s | Status: %-8s",
                internshipId, studentName, companyName, role, internshipMode, sDate, eDate, status);
    }
}
