import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;

/**
 * Modern FlatLaf Modal Dialog for Adding and Editing Student records.
 */
public class StudentFormDialog extends JDialog {
    private final boolean isEditMode;
    private final Student existingStudent;
    private final StudentDAO studentDAO;
    private final UserDAO userDAO;
    private boolean saved = false;

    private StyledTextField tfStudentId;
    private StyledTextField tfName;
    private JComboBox<String> cbDept;
    private JComboBox<Integer> cbYear;
    private StyledTextField tfCgpa;

    private JCheckBox chkCreateLogin;
    private StyledTextField tfUsername;
    private JPasswordField pfPassword;
    private JPanel pnlLoginAccount;
    private ToastNotification toast;

    public StudentFormDialog(Frame parent, Student student, StudentDAO studentDAO, UserDAO userDAO) {
        super(parent, (student == null) ? "Add New Student" : "Edit Student Details", true);
        this.isEditMode = (student != null);
        this.existingStudent = student;
        this.studentDAO = studentDAO;
        this.userDAO = userDAO;

        initComponents();
        if (isEditMode) {
            populateFields();
        }

        setSize(500, isEditMode ? 440 : 580);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents() {
        JPanel contentPane = new JPanel(new BorderLayout(0, 16));
        contentPane.setBackground(Theme.BG_APP);
        contentPane.setBorder(new EmptyBorder(20, 24, 20, 24));

        toast = new ToastNotification();
        contentPane.add(toast, BorderLayout.NORTH);

        // Form Card Panel
        CardPanel card = new CardPanel(10, new Insets(16, 20, 16, 20));
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 4, 6, 4);

        // 1. Student ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.35;
        JLabel lblId = new JLabel("Student ID *");
        lblId.setFont(Theme.FONT_BODY_BOLD);
        lblId.setForeground(Theme.TEXT_SECONDARY);
        card.add(lblId, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.65;
        tfStudentId = new StyledTextField(15);
        if (isEditMode) {
            tfStudentId.setEnabled(false);
        }
        card.add(tfStudentId, gbc);

        // 2. Full Name
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.35;
        JLabel lblName = new JLabel("Full Name *");
        lblName.setFont(Theme.FONT_BODY_BOLD);
        lblName.setForeground(Theme.TEXT_SECONDARY);
        card.add(lblName, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.65;
        tfName = new StyledTextField(20);
        card.add(tfName, gbc);

        // 3. Department
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.35;
        JLabel lblDept = new JLabel("Department *");
        lblDept.setFont(Theme.FONT_BODY_BOLD);
        lblDept.setForeground(Theme.TEXT_SECONDARY);
        card.add(lblDept, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.65;
        String[] depts = {
            "Computer Science", "Information Technology", "Data Science & AI",
            "Electronics & Comm", "Electrical & Electronics", "Mechanical Eng", "Civil Eng"
        };
        cbDept = new JComboBox<>(depts);
        cbDept.setFont(Theme.FONT_BODY);
        cbDept.setBackground(Theme.BG_INPUT);
        cbDept.setForeground(Theme.TEXT_PRIMARY);
        cbDept.setEditable(true);
        card.add(cbDept, gbc);

        // 4. Year
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.35;
        JLabel lblYear = new JLabel("Year of Study *");
        lblYear.setFont(Theme.FONT_BODY_BOLD);
        lblYear.setForeground(Theme.TEXT_SECONDARY);
        card.add(lblYear, gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.65;
        cbYear = new JComboBox<>(new Integer[]{1, 2, 3, 4});
        cbYear.setFont(Theme.FONT_BODY);
        cbYear.setBackground(Theme.BG_INPUT);
        cbYear.setForeground(Theme.TEXT_PRIMARY);
        card.add(cbYear, gbc);

        // 5. CGPA
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.35;
        JLabel lblCgpa = new JLabel("CGPA (0.0 - 10.0) *");
        lblCgpa.setFont(Theme.FONT_BODY_BOLD);
        lblCgpa.setForeground(Theme.TEXT_SECONDARY);
        card.add(lblCgpa, gbc);

        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 0.65;
        tfCgpa = new StyledTextField(10);
        card.add(tfCgpa, gbc);

        // 6. User Account Creation (Only on Add)
        if (!isEditMode) {
            gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
            chkCreateLogin = new JCheckBox("Create a student login account for this student");
            chkCreateLogin.setFont(Theme.FONT_BODY);
            chkCreateLogin.setForeground(Theme.TEXT_PRIMARY);
            chkCreateLogin.setOpaque(false);
            chkCreateLogin.setSelected(true);
            card.add(chkCreateLogin, gbc);

            gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
            pnlLoginAccount = new JPanel(new GridBagLayout());
            pnlLoginAccount.setOpaque(false);
            pnlLoginAccount.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_DEFAULT),
                    new EmptyBorder(8, 0, 0, 0)
            ));

            GridBagConstraints lgbc = new GridBagConstraints();
            lgbc.fill = GridBagConstraints.HORIZONTAL;
            lgbc.insets = new Insets(4, 4, 4, 4);

            lgbc.gridx = 0; lgbc.gridy = 0; lgbc.weightx = 0.35;
            JLabel lblUname = new JLabel("Username:");
            lblUname.setFont(Theme.FONT_BODY);
            lblUname.setForeground(Theme.TEXT_SECONDARY);
            pnlLoginAccount.add(lblUname, lgbc);

            lgbc.gridx = 1; lgbc.gridy = 0; lgbc.weightx = 0.65;
            tfUsername = new StyledTextField(15);
            pnlLoginAccount.add(tfUsername, lgbc);

            lgbc.gridx = 0; lgbc.gridy = 1; lgbc.weightx = 0.35;
            JLabel lblPwd = new JLabel("Password:");
            lblPwd.setFont(Theme.FONT_BODY);
            lblPwd.setForeground(Theme.TEXT_SECONDARY);
            pnlLoginAccount.add(lblPwd, lgbc);

            lgbc.gridx = 1; lgbc.gridy = 1; lgbc.weightx = 0.65;
            pfPassword = new JPasswordField(15);
            pfPassword.setFont(Theme.FONT_BODY);
            pfPassword.setForeground(Theme.TEXT_PRIMARY);
            pfPassword.setBackground(Theme.BG_INPUT);
            pfPassword.setCaretColor(Theme.ACCENT_PRIMARY);
            pfPassword.setText("student123");
            pfPassword.setPreferredSize(new Dimension(pfPassword.getPreferredSize().width, 38));
            pnlLoginAccount.add(pfPassword, lgbc);

            chkCreateLogin.addActionListener(e -> pnlLoginAccount.setVisible(chkCreateLogin.isSelected()));
            card.add(pnlLoginAccount, gbc);
        }

        contentPane.add(card, BorderLayout.CENTER);

        // Buttons Bar
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        StyledButton btnCancel = StyledButton.createSecondary("Cancel");
        btnCancel.addActionListener(e -> dispose());

        StyledButton btnSave = StyledButton.createPrimary(isEditMode ? "Save Changes" : "Add Student");
        btnSave.addActionListener(e -> handleSave());

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        contentPane.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(contentPane);
    }

    private void populateFields() {
        tfStudentId.setText(existingStudent.getStudentId());
        tfName.setText(existingStudent.getName());
        cbDept.setSelectedItem(existingStudent.getDepartment());
        cbYear.setSelectedItem(existingStudent.getYear());
        tfCgpa.setText(String.format("%.2f", existingStudent.getCgpa()));
    }

    private void handleSave() {
        String studentId = tfStudentId.getText().trim();
        String name = tfName.getText().trim();
        String dept = (cbDept.getSelectedItem() != null) ? cbDept.getSelectedItem().toString().trim() : "";
        Integer year = (Integer) cbYear.getSelectedItem();
        String cgpaStr = tfCgpa.getText().trim();

        if (studentId.isEmpty()) {
            toast.showError("Student ID cannot be empty.");
            tfStudentId.requestFocus();
            return;
        }

        if (name.isEmpty()) {
            toast.showError("Full Name cannot be empty.");
            tfName.requestFocus();
            return;
        }

        if (dept.isEmpty()) {
            toast.showError("Department cannot be empty.");
            return;
        }

        double cgpa;
        try {
            cgpa = Double.parseDouble(cgpaStr);
            if (cgpa < 0.0 || cgpa > 10.0) {
                toast.showError("CGPA must be between 0.0 and 10.0.");
                tfCgpa.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            toast.showError("Please enter a valid numeric CGPA (e.g. 8.5).");
            tfCgpa.requestFocus();
            return;
        }

        // Run DB operation off UI thread
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                if (isEditMode) {
                    existingStudent.setName(name);
                    existingStudent.setDepartment(dept);
                    existingStudent.setYear(year != null ? year : 1);
                    existingStudent.setCgpa(cgpa);
                    return studentDAO.updateStudent(existingStudent);
                } else {
                    if (studentDAO.getStudentById(studentId) != null) {
                        throw new StudentException("A student with ID '" + studentId + "' already exists!");
                    }

                    Integer userId = null;
                    if (chkCreateLogin != null && chkCreateLogin.isSelected()) {
                        String uname = tfUsername.getText().trim();
                        if (uname.isEmpty()) uname = studentId.toLowerCase();
                        String pwd = new String(pfPassword.getPassword()).trim();
                        if (pwd.isEmpty()) pwd = "student123";

                        if (userDAO.isUsernameTaken(uname)) {
                            throw new StudentException("Username '" + uname + "' is already taken.");
                        }

                        userId = userDAO.createUser(uname, pwd, "STUDENT", name);
                    }

                    Student newStudent = new Student(studentId, name, dept, year != null ? year : 1, cgpa, userId);
                    return studentDAO.addStudent(newStudent);
                }
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        saved = true;
                        dispose();
                    } else {
                        toast.showError("Failed to save student record.");
                    }
                } catch (Exception ex) {
                    String msg = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
                    toast.showError(msg);
                }
            }
        };
        worker.execute();
    }

    public boolean isSaved() {
        return saved;
    }
}
