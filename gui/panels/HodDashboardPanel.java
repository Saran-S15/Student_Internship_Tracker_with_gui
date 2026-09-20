import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * HOD Dashboard Panel: KPI Summary Cards, Records Tables, and TXT Report Generation.
 */
public class HodDashboardPanel extends JPanel {
    private final MainFrame mainFrame;
    private final InternshipDAO internshipDAO;
    private final InternshipManager internshipManager;
    private User currentUser;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private ToastNotification toast;
    private StatCard cardTotal;
    private StatCard cardApproved;
    private StatCard cardRejected;
    private StatCard cardPending;

    private JTabbedPane tabbedPane;

    private StyledTable tblApproved;
    private DefaultTableModel modelApproved;

    private StyledTable tblRejected;
    private DefaultTableModel modelRejected;

    private StyledTable tblStatus;
    private DefaultTableModel modelStatus;

    public HodDashboardPanel(MainFrame mainFrame, InternshipDAO internshipDAO) {
        this.mainFrame = mainFrame;
        this.internshipDAO = internshipDAO;
        this.internshipManager = new InternshipManager();
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(0, 16));
        setBackground(Theme.BG_APP);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        // 1. Top Section (Title + Toast)
        JPanel topBox = new JPanel(new BorderLayout(0, 12));
        topBox.setOpaque(false);

        JLabel lblTitle = new JLabel("HOD Department Overview & Reports");
        lblTitle.setFont(Theme.FONT_PAGE_HEADER);
        lblTitle.setForeground(Theme.TEXT_PRIMARY);

        toast = new ToastNotification();
        topBox.add(lblTitle, BorderLayout.NORTH);
        topBox.add(toast, BorderLayout.SOUTH);
        add(topBox, BorderLayout.NORTH);

        // 2. Center Section (KPI Cards + Tabs + Bottom Toolbar)
        JPanel centerBox = new JPanel(new BorderLayout(0, 16));
        centerBox.setOpaque(false);

        // 4 KPI Stat Cards
        JPanel cardsGrid = new JPanel(new GridLayout(1, 4, 16, 0));
        cardsGrid.setOpaque(false);

        cardTotal = new StatCard("Total Applications", "0", Theme.TEXT_PRIMARY, Theme.BG_CARD);
        cardApproved = new StatCard("Approved", "0", Theme.SUCCESS, new Color(0x06, 0x4E, 0x3B));
        cardRejected = new StatCard("Rejected", "0", Theme.DANGER, new Color(0x7F, 0x1D, 0x1D));
        cardPending = new StatCard("Pending Reviews", "0", Theme.WARNING, new Color(0x78, 0x35, 0x0F));

        cardsGrid.add(cardTotal);
        cardsGrid.add(cardApproved);
        cardsGrid.add(cardRejected);
        cardsGrid.add(cardPending);

        centerBox.add(cardsGrid, BorderLayout.NORTH);

        // Tabs
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Theme.FONT_BODY_BOLD);

        tabbedPane.addTab("✅  Approved Records", buildApprovedTab());
        tabbedPane.addTab("❌  Rejected Records", buildRejectedTab());
        tabbedPane.addTab("📈  Student Duration Status", buildStatusTab());

        tabbedPane.addChangeListener(e -> loadAllDataAsync());
        centerBox.add(tabbedPane, BorderLayout.CENTER);

        // Bottom Action Bar (Generate Report + Refresh)
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        bottomBar.setOpaque(false);

        StyledButton btnRefresh = StyledButton.createSecondary("Refresh Data");
        btnRefresh.addActionListener(e -> loadAllDataAsync());

        StyledButton btnReport = StyledButton.createPrimary("📄  Generate TXT Report");
        btnReport.setPreferredSize(new Dimension(btnReport.getPreferredSize().width, 40));
        btnReport.addActionListener(e -> handleGenerateReport());

        bottomBar.add(btnRefresh);
        bottomBar.add(btnReport);

        centerBox.add(bottomBar, BorderLayout.SOUTH);
        add(centerBox, BorderLayout.CENTER);
    }

    private JPanel buildApprovedTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(8, 0, 0, 0));

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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(8, 0, 0, 0));

        String[] columns = {"ID", "Student ID", "Student Name", "Company", "Role", "Mode", "Start Date", "End Date", "Status", "Rejection Reason"};
        modelRejected = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tblRejected = new StyledTable(modelRejected);
        tblRejected.getColumnModel().getColumn(8).setCellRenderer(new StatusBadgeRenderer());
        panel.add(tblRejected.createScrollPane(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildStatusTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(8, 0, 0, 0));

        String[] columns = {"ID", "Student ID", "Student Name", "Company", "Role", "Duration", "Status"};
        modelStatus = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tblStatus = new StyledTable(modelStatus);
        tblStatus.getColumnModel().getColumn(6).setCellRenderer(new StatusBadgeRenderer());
        panel.add(tblStatus.createScrollPane(), BorderLayout.CENTER);
        return panel;
    }

    public void setUser(User user) {
        this.currentUser = user;
        tabbedPane.setSelectedIndex(0);
        loadAllDataAsync();
    }

    private void loadAllDataAsync() {
        toast.hideToast();

        // 1. Load Overview Counts
        SwingWorker<Map<String, Integer>, Void> countsWorker = new SwingWorker<>() {
            @Override
            protected Map<String, Integer> doInBackground() throws Exception {
                return internshipDAO.getOverviewCounts();
            }

            @Override
            protected void done() {
                try {
                    Map<String, Integer> counts = get();
                    int total = counts.getOrDefault("TOTAL", 0);
                    int approved = counts.getOrDefault("APPROVED", 0);
                    int rejected = counts.getOrDefault("REJECTED", 0);
                    int pending = counts.getOrDefault("PENDING", 0);

                    cardTotal.setValue(String.valueOf(total));
                    cardApproved.setValue(String.valueOf(approved));
                    cardRejected.setValue(String.valueOf(rejected));
                    cardPending.setValue(String.valueOf(pending));

                    double rate = (total > 0) ? ((double) approved / total) * 100 : 0.0;
                    cardApproved.setSubtext(String.format("Approval Rate: %.1f%%", rate));
                } catch (Exception e) {
                    toast.showError("Failed to load counts: " + e.getMessage());
                }
            }
        };
        countsWorker.execute();

        // 2. Load Table Records
        SwingWorker<List<Internship>, Void> tableWorker = new SwingWorker<>() {
            @Override
            protected List<Internship> doInBackground() throws Exception {
                return internshipDAO.getAllInternships();
            }

            @Override
            protected void done() {
                try {
                    List<Internship> allList = get();

                    modelApproved.setRowCount(0);
                    modelRejected.setRowCount(0);
                    modelStatus.setRowCount(0);

                    for (Internship in : allList) {
                        String sDate = (in.getStartDate() != null) ? in.getStartDate().format(DATE_FMT) : "-";
                        String eDate = (in.getEndDate() != null) ? in.getEndDate().format(DATE_FMT) : "-";
                        String remark = (in.getRemark() != null && !in.getRemark().isEmpty()) ? in.getRemark() : "-";

                        if ("Approved".equalsIgnoreCase(in.getStatus())) {
                            modelApproved.addRow(new Object[]{
                                in.getInternshipId(), in.getStudentId(), in.getStudentName(),
                                in.getCompanyName(), in.getRole(), in.getInternshipMode(),
                                sDate, eDate, in.getStatus(), remark
                            });
                        } else if ("Rejected".equalsIgnoreCase(in.getStatus())) {
                            modelRejected.addRow(new Object[]{
                                in.getInternshipId(), in.getStudentId(), in.getStudentName(),
                                in.getCompanyName(), in.getRole(), in.getInternshipMode(),
                                sDate, eDate, in.getStatus(), remark
                            });
                        }

                        modelStatus.addRow(new Object[]{
                            in.getInternshipId(), in.getStudentId(), in.getStudentName(),
                            in.getCompanyName(), in.getRole(), in.getDurationString(), in.getStatus()
                        });
                    }
                } catch (Exception e) {
                    toast.showError("Failed to load records: " + e.getMessage());
                }
            }
        };
        tableWorker.execute();
    }

    private void handleGenerateReport() {
        internshipManager.generateTxtReport();
        File file = new File("D:\\InternshipTracker\\Internship_Report.txt");
        if (file.exists()) {
            int opt = JOptionPane.showOptionDialog(
                    mainFrame,
                    "Internship Report successfully generated!\nLocation: " + file.getAbsolutePath() + "\n\nWould you like to open the report file now?",
                    "Report Generated",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Open Report", "OK"},
                    "Open Report"
            );

            if (opt == JOptionPane.YES_OPTION) {
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(file);
                    }
                } catch (Exception ex) {
                    toast.showInfo("Report exported at: " + file.getAbsolutePath());
                }
            }
        } else {
            toast.showError("Report file could not be verified.");
        }
    }
}
