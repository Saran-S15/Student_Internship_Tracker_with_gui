import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student CRUD operations.
 */
public class StudentDAO {

    /**
     * Adds a new student record to the database.
     */
    public boolean addStudent(Student student) throws SQLException, StudentException {
        if (getStudentById(student.getStudentId()) != null) {
            throw new StudentException("Student with ID '" + student.getStudentId() + "' already exists.");
        }

        String sql = "INSERT INTO students (student_id, name, department, year, cgpa, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getName());
            pstmt.setString(3, student.getDepartment());
            pstmt.setInt(4, student.getYear());
            pstmt.setDouble(5, student.getCgpa());
            
            if (student.getUserId() != null) {
                pstmt.setInt(6, student.getUserId());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Retrieves all student records.
     */
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT student_id, name, department, year, cgpa, user_id FROM students ORDER BY student_id";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Student s = mapResultSetToStudent(rs);
                students.add(s);
            }
        }
        return students;
    }

    /**
     * Finds a student by exact student ID.
     */
    public Student getStudentById(String studentId) throws SQLException {
        String sql = "SELECT student_id, name, department, year, cgpa, user_id FROM students WHERE student_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
            }
        }
        return null;
    }

    /**
     * Searches students by ID, Name, or Department keyword.
     */
    public List<Student> searchStudents(String keyword) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT student_id, name, department, year, cgpa, user_id FROM students " +
                     "WHERE student_id LIKE ? OR name LIKE ? OR department LIKE ? ORDER BY student_id";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            String pattern = "%" + keyword + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            pstmt.setString(3, pattern);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapResultSetToStudent(rs));
                }
            }
        }
        return students;
    }

    /**
     * Finds student profile linked to a specific user_id.
     */
    public Student getStudentByUserId(int userId) throws SQLException {
        String sql = "SELECT student_id, name, department, year, cgpa, user_id FROM students WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                }
            }
        }
        return null;
    }

    /**
     * Updates an existing student record.
     */
    public boolean updateStudent(Student student) throws SQLException, StudentException {
        if (getStudentById(student.getStudentId()) == null) {
            throw new StudentException("Student with ID '" + student.getStudentId() + "' not found.");
        }

        String sql = "UPDATE students SET name = ?, department = ?, year = ?, cgpa = ? WHERE student_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getDepartment());
            pstmt.setInt(3, student.getYear());
            pstmt.setDouble(4, student.getCgpa());
            pstmt.setString(5, student.getStudentId());
            
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Deletes a student record by student ID.
     */
    public boolean deleteStudent(String studentId) throws SQLException, StudentException {
        Student student = getStudentById(studentId);
        if (student == null) {
            throw new StudentException("Student with ID '" + studentId + "' not found.");
        }

        String sql = "DELETE FROM students WHERE student_id = ?";
        boolean deleted = false;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, studentId);
            deleted = pstmt.executeUpdate() > 0;
        }

        // Also clean up linked user login record if one exists
        if (deleted && student.getUserId() != null) {
            UserDAO userDAO = new UserDAO();
            userDAO.deleteUser(student.getUserId());
        }

        return deleted;
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setStudentId(rs.getString("student_id"));
        s.setName(rs.getString("name"));
        s.setDepartment(rs.getString("department"));
        s.setYear(rs.getInt("year"));
        s.setCgpa(rs.getDouble("cgpa"));
        int uId = rs.getInt("user_id");
        if (!rs.wasNull()) {
            s.setUserId(uId);
        }
        return s;
    }
}
