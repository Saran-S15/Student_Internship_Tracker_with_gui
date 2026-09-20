import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Internship CRUD and Workflow operations.
 */
public class InternshipDAO {

    /**
     * Submits a new internship application (default status = 'Pending').
     */
    public boolean submitInternship(Internship internship) throws SQLException {
        String sql = "INSERT INTO internships (student_id, student_name, company_name, company_location, " +
                     "domain, role, internship_mode, start_date, end_date, status, remark) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, internship.getStudentId());
            pstmt.setString(2, internship.getStudentName());
            pstmt.setString(3, internship.getCompanyName());
            pstmt.setString(4, internship.getCompanyLocation());
            pstmt.setString(5, internship.getDomain());
            pstmt.setString(6, internship.getRole());
            pstmt.setString(7, internship.getInternshipMode());
            pstmt.setDate(8, Date.valueOf(internship.getStartDate()));
            pstmt.setDate(9, Date.valueOf(internship.getEndDate()));
            pstmt.setString(10, internship.getStatus() != null ? internship.getStatus() : "Pending");
            
            if (internship.getRemark() != null) {
                pstmt.setString(11, internship.getRemark());
            } else {
                pstmt.setNull(11, Types.VARCHAR);
            }
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Retrieves all internships submitted by a specific student (by student_id).
     */
    public List<Internship> getInternshipsByStudentId(String studentId) throws SQLException {
        List<Internship> list = new ArrayList<>();
        String sql = "SELECT * FROM internships WHERE student_id = ? ORDER BY internship_id DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToInternship(rs));
                }
            }
        }
        return list;
    }

    /**
     * Retrieves all internships matching a specific status ('Pending', 'Approved', 'Rejected').
     */
    public List<Internship> getInternshipsByStatus(String status) throws SQLException {
        List<Internship> list = new ArrayList<>();
        String sql = "SELECT * FROM internships WHERE status = ? ORDER BY internship_id ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToInternship(rs));
                }
            }
        }
        return list;
    }

    /**
     * Retrieves all internship records.
     */
    public List<Internship> getAllInternships() throws SQLException {
        List<Internship> list = new ArrayList<>();
        String sql = "SELECT * FROM internships ORDER BY internship_id ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapResultSetToInternship(rs));
            }
        }
        return list;
    }

    /**
     * Finds a single internship by its ID.
     */
    public Internship getInternshipById(int internshipId) throws SQLException {
        String sql = "SELECT * FROM internships WHERE internship_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, internshipId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInternship(rs);
                }
            }
        }
        return null;
    }

    /**
     * Updates status and remark for an internship record (Approve/Reject).
     */
    public boolean updateStatusAndRemark(int internshipId, String status, String remark) throws SQLException {
        String sql = "UPDATE internships SET status = ?, remark = ? WHERE internship_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, status);
            if (remark != null && !remark.trim().isEmpty()) {
                pstmt.setString(2, remark.trim());
            } else {
                pstmt.setNull(2, Types.VARCHAR);
            }
            pstmt.setInt(3, internshipId);
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Fetches counts of Total, Approved, Rejected, and Pending internships.
     */
    public Map<String, Integer> getOverviewCounts() throws SQLException {
        Map<String, Integer> counts = new LinkedHashMap<>();
        counts.put("TOTAL", 0);
        counts.put("APPROVED", 0);
        counts.put("REJECTED", 0);
        counts.put("PENDING", 0);

        String sql = "SELECT status, COUNT(*) AS cnt FROM internships GROUP BY status";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            int total = 0;
            while (rs.next()) {
                String st = rs.getString("status");
                int c = rs.getInt("cnt");
                total += c;
                if (st != null) {
                    counts.put(st.toUpperCase(), c);
                }
            }
            counts.put("TOTAL", total);
        }
        return counts;
    }

    private Internship mapResultSetToInternship(ResultSet rs) throws SQLException {
        Date sDate = rs.getDate("start_date");
        Date eDate = rs.getDate("end_date");
        
        return new Internship(
                rs.getInt("internship_id"),
                rs.getString("student_id"),
                rs.getString("student_name"),
                rs.getString("company_name"),
                rs.getString("company_location"),
                rs.getString("domain"),
                rs.getString("role"),
                rs.getString("internship_mode"),
                sDate != null ? sDate.toLocalDate() : null,
                eDate != null ? eDate.toLocalDate() : null,
                rs.getString("status"),
                rs.getString("remark")
        );
    }
}
