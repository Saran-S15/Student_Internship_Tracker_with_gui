import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Faculty Dashboard Panel: Review Pending Applications (Approve / Reject with Modal / Skip), View Approved, View Rejected.
 */
public class FacultyDashboardPanel extends JPanel {
    private final MainFrame mainFrame;
    private final InternshipDAO internshipDAO;
    private User currentUser;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private JTabbedPane tabbedPane;
    private ToastNotification toast;

    // Tables
    private StyledTable tblPending;
    private DefaultTableModel modelPending;

    private StyledTable tblApproved;
    private DefaultTableModel modelApproved;

    private StyledTable tblRejected;
    private DefaultTableModel modelRejected;

    public FacultyDashboardPanel(MainFrame mainFrame, InternshipDAO internshipDAO) {
        this.mainFrame = mainFrame;
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

        JLabel lblTitle = new JLabel("Faculty Review & Approval Portal");
        lblTitle.setFont(Theme.FONT_PAGE_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);

        toast = new ToastNotification();
        topBox.add(lblTitle, BorderLayout.NORTH);
        topBox.add(toast, BorderLayout.SOUTH);
        add(topBox, BorderLayout.NORTH);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Theme.FONT_BODY_BOLD);

        tabbedPane.addTab("⏳  Pending Applications", buildPendingTab());
        tabbedPane.addTab("✅  Approved Internships", buildApprovedTab());
        tabbedPane.addTab("❌  Rejected Internships", buildRejectedTab());

        tabbedPane.addChangeListener(e -> {
            int idx = tabbedPane.getSelectedIndex();
            if (idx == 0) loadPendingAsync();
            else if (idx == 1) loadApprovedAsync();
            else if (idx == 2) loadRejectedAsync();
        });

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel buildPendingTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(12, 0, 0, 0));

        String[] columns = {"ID", "Student ID", "Student Name", "Company", "Location", "Domain", "Role", "Mode", "Start Date", "End Date", "Duration"};
        modelPending = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tblPending = new StyledTable(modelPending);
        panel.add(tblPending.createScrollPane(), BorderLayout.CENTER);

        // Action Toolbar
        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.setOpaque(false);

        JLabel lblHint = new JLabel("Select an application above to take action:");
        lblHint.setFont(Theme.FONT_BODY);
        lblHint.setForeground(Theme.TEXT_SECONDARY);
        bottomBar.add(lblHint, BorderLayout.WEST);

        JPanel btnGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnGroup.setOpaque(false);

        StyledButton btnApprove = StyledButton.createSuccess("✓  Approve");
        btnApprove.addActionListener(e -> handleApprove());

        StyledButton btnReject = StyledButton.createDanger("✕  Reject");
        btnReject.addActionListener(e -> handleReject());

        StyledButton btnSkip = StyledButton.createSecondary("Skip / Next");
        btnSkip.addActionListener(e -> handleSkip());

        StyledButton btnRefresh = StyledButton.createSecondary("Refresh");
        btnRefresh.addActionListener(e -> loadPendingAsync());

        btnGroup.add(btnApprove);
        btnGroup.add(btnReject);
        btnGroup.add(btnSkip);
        btnGroup.add(btnRefresh);

        bottomBar.add(btnGroup, BorderLayout.EAST);
        panel.add(bottomBar, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildApprovedTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(12, 0, 0, 0));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        top.setOpaque(false);
        StyledButton btnRefresh = StyledButton.createSecondary("Refresh");
        btnRefresh.addActionListener(e -> loadApprovedAsync());
        top.add(btnRefresh);
        panel.add(top, BorderLayout.NORTH);

        String[] columns = {"ID", "Student ID", "Student Name", "Company", "Role", "Mode", "Start Date", "End Date", "Status", "Remarks"};
        modelApproved = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tblApproved = new StyledTable(modelApproved);
        tblApproved.getColumnModel().getColumn(8).setCellRenderer(new StatusBadgeRenderer());
        panel.add(tblApproved.createScrollPane(), BorderLayout.CENTER);

        return panel;
    }

    private JPanel buildRejectedTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(12, 0, 0, 0));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        top.setOpaque(false);
        StyledButton btnRefresh = StyledButton.createSecondary("Refresh");
        btnRefresh.addActionListener(e -> loadRejectedAsync());
        top.add(btnRefresh);
        panel.add(top, BorderLayout.NORTH);

        String[] columns = {"ID", "Student ID", "Student Name", "Company", "Role", "Mode", "Start Date", "End Date", "Status", "Rejection Reason"};
        modelRejected = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tblRejected = new StyledTable(modelRejected);
        tblRejected.getColumnModel().getColumn(8).setCellRenderer(new StatusBadgeRenderer());
        panel.add(tblRejected.createScrollPane(), BorderLayout.CENTER);

        return panel;
    }

    public void setUser(User user) {
        this.currentUser = user;
        tabbedPane.setSelectedIndex(0);
        loadPendingAsync();
    }

    private void loadPendingAsync() {
        toast.hideToast();
        SwingWorker<List<Internship>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Internship> doInBackground() throws Exception {
                return internshipDAO.getInternshipsByStatus("Pending");
            }

            @Override
            protected void done() {
                try {
                    List<Internship> list = get();
                    modelPending.setRowCount(0);
                    for (Internship in : list) {
                        String sDate = (in.getStartDate() != null) ? in.getStartDate().format(DATE_FMT) : "-";
                        String eDate = (in.getEndDate() != null) ? in.getEndDate().format(DATE_FMT) : "-";
                        modelPending.addRow(new Object[]{
                            in.getInternshipId(),
                            in.getStudentId(),
                            in.getStudentName(),
                            in.getCompanyName(),
                            in.getCompanyLocation(),
                            in.getDomain(),
                            in.getRole(),
                            in.getInternshipMode(),
                            sDate,
                            eDate,
                            in.getDurationString()
                        });
                    }
                    if (modelPending.getRowCount() > 0) {
                        tblPending.setRowSelectionInterval(0, 0);
                    }
                } catch (Exception e) {
                    toast.showError("Failed to load pending reviews: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void loadApprovedAsync() {
        SwingWorker<List<Internship>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Internship> doInBackground() throws Exception {
                return internshipDAO.getInternshipsByStatus("Approved");
            }

            @Override
            protected void done() {
                try {
                    List<Internship> list = get();
                    modelApproved.setRowCount(0);
                    for (Internship in : list) {
                        String sDate = (in.getStartDate() != null) ? in.getStartDate().format(DATE_FMT) : "-";
                        String eDate = (in.getEndDate() != null) ? in.getEndDate().format(DATE_FMT) : "-";
                        String remark = (in.getRemark() != null && !in.getRemark().isEmpty()) ? in.getRemark() : "-";
                        modelApproved.addRow(new Object[]{
                            in.getInternshipId(),
                            in.getStudentId(),
                            in.getStudentName(),
                            in.getCompanyName(),
                            in.getRole(),
                            in.getInternshipMode(),
                            sDate,
                            eDate,
                            in.getStatus(),
                            remark
                        });
                    }
                } catch (Exception e) {
                    toast.showError("Failed to load approved internships: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void loadRejectedAsync() {
        SwingWorker<List<Internship>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Internship> doInBackground() throws Exception {
                return internshipDAO.getInternshipsByStatus("Rejected");
            }

            @Override
            protected void done() {
                try {
                    List<Internship> list = get();
                    modelRejected.setRowCount(0);
                    for (Internship in : list) {
                        String sDate = (in.getStartDate() != null) ? in.getStartDate().format(DATE_FMT) : "-";
                        String eDate = (in.getEndDate() != null) ? in.getEndDate().format(DATE_FMT) : "-";
                        String remark = (in.getRemark() != null && !in.getRemark().isEmpty()) ? in.getRemark() : "-";
                        modelRejected.addRow(new Object[]{
                            in.getInternshipId(),
                            in.getStudentId(),
                            in.getStudentName(),
                            in.getCompanyName(),
                            in.getRole(),
                            in.getInternshipMode(),
                            sDate,
                            eDate,
                            in.getStatus(),
                            remark
                        });
                    }
                } catch (Exception e) {
                    toast.showError("Failed to load rejected internships: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void handleApprove() {
        int row = tblPending.getSelectedRow();
        if (row < 0) {
            toast.showInfo("Please select a pending application from the table to approve.");
            return;
        }

        int internshipId = (int) modelPending.getValueAt(row, 0);
        String studentName = (String) modelPending.getValueAt(row, 2);
        String company = (String) modelPending.getValueAt(row, 3);

        // Approve directly without prompting for remark
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return internshipDAO.updateStatusAndRemark(internshipId, "Approved", "Approved by Faculty");
            }

            @Override
            protected void done() {
                try {
                    boolean ok = get();
                    if (ok) {
                        toast.showSuccess("Application for " + studentName + " (" + company + ") has been APPROVED.");
                        loadPendingAsync();
                    } else {
                        toast.showError("Failed to approve application.");
                    }
                } catch (Exception e) {
                    toast.showError("Database Error: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void handleReject() {
        int row = tblPending.getSelectedRow();
        if (row < 0) {
            toast.showInfo("Please select a pending application from the table to reject.");
            return;
        }

        int internshipId = (int) modelPending.getValueAt(row, 0);
        String studentName = (String) modelPending.getValueAt(row, 2);
        String company = (String) modelPending.getValueAt(row, 3);

        RemarkModalDialog dialog = new RemarkModalDialog(mainFrame, studentName, company);
        String remark = dialog.showDialog();

        if (remark != null && !remark.trim().isEmpty()) {
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    return internshipDAO.updateStatusAndRemark(internshipId, "Rejected", remark.trim());
                }

                @Override
                protected void done() {
                    try {
                        boolean ok = get();
                        if (ok) {
                            toast.showSuccess("Application for " + studentName + " has been REJECTED.");
                            loadPendingAsync();
                        } else {
                            toast.showError("Failed to reject application.");
                        }
                    } catch (Exception e) {
                        toast.showError("Database Error: " + e.getMessage());
                    }
                }
            };
            worker.execute();
        }
    }

    private void handleSkip() {
        int row = tblPending.getSelectedRow();
        int total = tblPending.getRowCount();
        if (total > 0) {
            int next = (row + 1) % total;
            tblPending.setRowSelectionInterval(next, next);
        }
    }
}
