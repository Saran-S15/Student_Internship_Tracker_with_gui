import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Student Dashboard Panel: Profile Card, Submit Application Form (DD-MM-YYYY), My Internships Table.
 */
public class StudentDashboardPanel extends JPanel {
    private final MainFrame mainFrame;
    private final StudentDAO studentDAO;
    private final InternshipDAO internshipDAO;
    private User currentUser;
    private Student currentStudent;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private JTabbedPane tabbedPane;
    private ToastNotification toast;

    // Profile Labels
    private JLabel valId;
    private JLabel valName;
    private JLabel valUser;
    private JLabel valDept;
    private JLabel valYear;
    private JLabel valCgpa;

    // Submit Form
    private StyledTextField tfCompany;
    private StyledTextField tfLocation;
    private StyledTextField tfDomain;
    private StyledTextField tfRole;
    private JComboBox<String> cbMode;
    private StyledTextField tfStartDate;
    private StyledTextField tfEndDate;
    private StyledButton btnSubmit;

    // My Internships Table
    private StyledTable tblMyInternships;
    private DefaultTableModel tableModel;

    public StudentDashboardPanel(MainFrame mainFrame, StudentDAO studentDAO, InternshipDAO internshipDAO) {
        this.mainFrame = mainFrame;
        this.studentDAO = studentDAO;
        this.internshipDAO = internshipDAO;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.BG_APP);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        // Top Title + Toast
        JPanel topBox = new JPanel(new BorderLayout(0, 12));
        topBox.setOpaque(false);

        JLabel lblTitle = new JLabel("Student Internship Portal");
        lblTitle.setFont(Theme.FONT_PAGE_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);

        toast = new ToastNotification();
        topBox.add(lblTitle, BorderLayout.NORTH);
        topBox.add(toast, BorderLayout.SOUTH);
        add(topBox, BorderLayout.NORTH);

        // Center Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Theme.FONT_BODY_BOLD);

        tabbedPane.addTab("👤  My Profile", buildProfileTab());
        tabbedPane.addTab("📝  Submit Application", buildSubmitTab());
        tabbedPane.addTab("📋  My Applications", buildHistoryTab());

        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 2) {
                loadMyInternshipsAsync();
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel buildProfileTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        CardPanel card = new CardPanel(12, new Insets(28, 36, 28, 36));
        card.setPreferredSize(new Dimension(560, 380));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 8, 10, 8);

        JLabel lblHeader = new JLabel("Academic & Account Profile");
        lblHeader.setFont(Theme.FONT_SUBHEADER);
        lblHeader.setForeground(Theme.ACCENT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(lblHeader, gbc);

        gbc.gridwidth = 1;
        valId = addProfileRow(card, "Student ID:", 1, gbc);
        valName = addProfileRow(card, "Full Name:", 2, gbc);
        valUser = addProfileRow(card, "Username:", 3, gbc);
        valDept = addProfileRow(card, "Department:", 4, gbc);
        valYear = addProfileRow(card, "Year of Study:", 5, gbc);
        valCgpa = addProfileRow(card, "Current CGPA:", 6, gbc);

        panel.add(card);
        return panel;
    }

    private JLabel addProfileRow(JPanel card, String labelText, int row, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.4;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(Theme.FONT_BODY_BOLD);
        lbl.setForeground(Theme.TEXT_SECONDARY);
        card.add(lbl, gbc);

        gbc.gridx = 1; gbc.gridy = row; gbc.weightx = 0.6;
        JLabel val = new JLabel("-");
        val.setFont(Theme.FONT_BODY);
        val.setForeground(Theme.TEXT_PRIMARY);
        card.add(val, gbc);
        return val;
    }

    private JPanel buildSubmitTab() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        CardPanel card = new CardPanel(12, new Insets(24, 32, 24, 32));
        card.setPreferredSize(new Dimension(660, 520));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 6, 6, 6);

        JLabel lblH = new JLabel("Internship Application Form");
        lblH.setFont(Theme.FONT_SUBHEADER);
        lblH.setForeground(Theme.ACCENT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(lblH, gbc);

        gbc.gridwidth = 1;

        // Company
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.35;
        card.add(createFormLabel("Company Name *"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.65;
        tfCompany = new StyledTextField(20);
        card.add(tfCompany, gbc);

        // Location
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.35;
        card.add(createFormLabel("Company Location *"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.65;
        tfLocation = new StyledTextField(20);
        card.add(tfLocation, gbc);

        // Domain
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.35;
        card.add(createFormLabel("Domain / Tech *"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.65;
        tfDomain = new StyledTextField(20);
        card.add(tfDomain, gbc);

        // Role
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.35;
        card.add(createFormLabel("Role / Designation *"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 0.65;
        tfRole = new StyledTextField(20);
        card.add(tfRole, gbc);

        // Mode
        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.35;
        card.add(createFormLabel("Internship Mode *"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 0.65;
        cbMode = new JComboBox<>(new String[]{"Online", "Offline", "Hybrid"});
        cbMode.setFont(Theme.FONT_BODY);
        cbMode.setBackground(Theme.BG_INPUT);
        cbMode.setForeground(Theme.TEXT_PRIMARY);
        cbMode.setSelectedItem("Hybrid");
        card.add(cbMode, gbc);

        // Start Date
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.35;
        card.add(createFormLabel("Start Date (DD-MM-YYYY) *"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; gbc.weightx = 0.65;
        tfStartDate = new StyledTextField(15);
        tfStartDate.setToolTipText("e.g. 01-06-2026");
        card.add(tfStartDate, gbc);

        // End Date
        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0.35;
        card.add(createFormLabel("End Date (DD-MM-YYYY) *"), gbc);
        gbc.gridx = 1; gbc.gridy = 7; gbc.weightx = 0.65;
        tfEndDate = new StyledTextField(15);
        tfEndDate.setToolTipText("e.g. 31-08-2026");
        card.add(tfEndDate, gbc);

        // Submit Button
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.insets = new Insets(16, 6, 6, 6);
        btnSubmit = StyledButton.createPrimary("Submit Application");
        btnSubmit.setPreferredSize(new Dimension(btnSubmit.getPreferredSize().width, 40));
        btnSubmit.addActionListener(e -> handleSubmitInternship());
        card.add(btnSubmit, gbc);

        wrapper.add(card);
        return wrapper;
    }

    private JLabel createFormLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.FONT_BODY_BOLD);
        lbl.setForeground(Theme.TEXT_SECONDARY);
        return lbl;
    }

    private JPanel buildHistoryTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(12, 0, 0, 0));

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topBar.setOpaque(false);
        StyledButton btnRefresh = StyledButton.createSecondary("Refresh");
        btnRefresh.addActionListener(e -> loadMyInternshipsAsync());
        topBar.add(btnRefresh);
        panel.add(topBar, BorderLayout.NORTH);

        String[] columns = {"ID", "Company", "Location", "Domain", "Role", "Mode", "Start Date", "End Date", "Status", "Remarks"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        tblMyInternships = new StyledTable(tableModel);
        tblMyInternships.getColumnModel().getColumn(8).setCellRenderer(new StatusBadgeRenderer());

        panel.add(tblMyInternships.createScrollPane(), BorderLayout.CENTER);
        return panel;
    }

    public void setUser(User user) {
        this.currentUser = user;
        loadProfileDataAsync();
        loadMyInternshipsAsync();
        tabbedPane.setSelectedIndex(0);
    }

    private void loadProfileDataAsync() {
        SwingWorker<Student, Void> worker = new SwingWorker<>() {
            @Override
            protected Student doInBackground() throws Exception {
                Student s = studentDAO.getStudentByUserId(currentUser.getUserId());
                if (s == null) {
                    List<Student> list = studentDAO.searchStudents(currentUser.getFullName());
                    if (!list.isEmpty()) s = list.get(0);
                }
                return s;
            }

            @Override
            protected void done() {
                try {
                    currentStudent = get();
                    if (currentStudent != null) {
                        valId.setText(currentStudent.getStudentId());
                        valName.setText(currentStudent.getName());
                        valUser.setText(currentUser.getUsername());
                        valDept.setText(currentStudent.getDepartment());
                        valYear.setText("Year " + currentStudent.getYear());
                        valCgpa.setText(String.format("%.2f / 10.0", currentStudent.getCgpa()));
                    } else {
                        valId.setText("STU-" + currentUser.getUserId());
                        valName.setText(currentUser.getFullName());
                        valUser.setText(currentUser.getUsername());
                        valDept.setText("Not Assigned");
                        valYear.setText("-");
                        valCgpa.setText("-");
                    }
                } catch (Exception e) {
                    toast.showError("Failed to load profile: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void handleSubmitInternship() {
        toast.hideToast();
        String company = tfCompany.getText().trim();
        String location = tfLocation.getText().trim();
        String domain = tfDomain.getText().trim();
        String role = tfRole.getText().trim();
        String mode = (String) cbMode.getSelectedItem();
        String startStr = tfStartDate.getText().trim();
        String endStr = tfEndDate.getText().trim();

        if (company.isEmpty() || location.isEmpty() || domain.isEmpty() || role.isEmpty() || startStr.isEmpty() || endStr.isEmpty()) {
            toast.showError("Please fill in all required fields.");
            return;
        }

        LocalDate startDate;
        try {
            startDate = LocalDate.parse(startStr, DATE_FMT);
        } catch (DateTimeParseException e) {
            toast.showError("Invalid Start Date format. Please use DD-MM-YYYY (e.g. 01-06-2026).");
            tfStartDate.requestFocus();
            return;
        }

        LocalDate endDate;
        try {
            endDate = LocalDate.parse(endStr, DATE_FMT);
        } catch (DateTimeParseException e) {
            toast.showError("Invalid End Date format. Please use DD-MM-YYYY (e.g. 31-08-2026).");
            tfEndDate.requestFocus();
            return;
        }

        if (endDate.isBefore(startDate)) {
            toast.showError("End Date cannot be before Start Date.");
            return;
        }

        String studentId = (currentStudent != null) ? currentStudent.getStudentId() : "STU-" + currentUser.getUserId();
        String studentName = (currentStudent != null) ? currentStudent.getName() : currentUser.getFullName();

        Internship in = new Internship(studentId, studentName, company, location, domain, role, mode, startDate, endDate);

        btnSubmit.setEnabled(false);
        btnSubmit.setText("Submitting...");

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return internshipDAO.submitInternship(in);
            }

            @Override
            protected void done() {
                btnSubmit.setEnabled(true);
                btnSubmit.setText("Submit Application");
                try {
                    boolean ok = get();
                    if (ok) {
                        toast.showSuccess("Application submitted successfully! Status: Pending");
                        tfCompany.setText("");
                        tfLocation.setText("");
                        tfDomain.setText("");
                        tfRole.setText("");
                        tfStartDate.setText("");
                        tfEndDate.setText("");
                        cbMode.setSelectedIndex(0);
                        tabbedPane.setSelectedIndex(2);
                        loadMyInternshipsAsync();
                    } else {
                        toast.showError("Failed to submit internship application.");
                    }
                } catch (Exception e) {
                    toast.showError("Error: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void loadMyInternshipsAsync() {
        String studentId = (currentStudent != null) ? currentStudent.getStudentId() : "STU-" + currentUser.getUserId();
        SwingWorker<List<Internship>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Internship> doInBackground() throws Exception {
                return internshipDAO.getInternshipsByStudentId(studentId);
            }

            @Override
            protected void done() {
                try {
                    List<Internship> list = get();
                    tableModel.setRowCount(0);
                    for (Internship in : list) {
                        String sDate = (in.getStartDate() != null) ? in.getStartDate().format(DATE_FMT) : "-";
                        String eDate = (in.getEndDate() != null) ? in.getEndDate().format(DATE_FMT) : "-";
                        String remark = (in.getRemark() != null && !in.getRemark().isEmpty()) ? in.getRemark() : "-";
                        tableModel.addRow(new Object[]{
                            in.getInternshipId(),
                            in.getCompanyName(),
                            in.getCompanyLocation(),
                            in.getDomain(),
                            in.getRole(),
                            in.getInternshipMode(),
                            sDate,
                            eDate,
                            in.getStatus(),
                            remark
                        });
                    }
                } catch (Exception e) {
                    toast.showError("Failed to load applications: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }
}
