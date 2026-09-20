import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Main Application Window Frame with fixed sidebar navigation and CardLayout content manager.
 */
public class MainFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel contentCards;
    private final JPanel sidebarPanel;

    // DAOs
    private final UserDAO userDAO;
    private final StudentDAO studentDAO;
    private final InternshipDAO internshipDAO;

    // Panels
    private LoginPanel loginPanel;
    private AdminDashboardPanel adminPanel;
    private StudentDashboardPanel studentPanel;
    private FacultyDashboardPanel facultyPanel;
    private HodDashboardPanel hodPanel;

    // Sidebar UI Components
    private JLabel lblUserFullName;
    private JLabel lblUserRole;
    private JPanel navButtonsBox;
    private StyledButton btnSidebarActive;

    private User currentUser;

    public MainFrame() {
        super("Student Internship Tracker");

        this.userDAO = new UserDAO();
        this.studentDAO = new StudentDAO();
        this.internshipDAO = new InternshipDAO();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1040, 720));
        setPreferredSize(new Dimension(1200, 780));
        getContentPane().setBackground(Theme.BG_APP);

        // Main Layout Container
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(Theme.BG_APP);

        // 1. Fixed Left Sidebar
        sidebarPanel = buildSidebar();
        sidebarPanel.setVisible(false); // Hidden on login screen
        mainContainer.add(sidebarPanel, BorderLayout.WEST);

        // 2. Center Content Cards
        cardLayout = new CardLayout();
        contentCards = new JPanel(cardLayout);
        contentCards.setBackground(Theme.BG_APP);

        loginPanel = new LoginPanel(this, userDAO);
        adminPanel = new AdminDashboardPanel(this, studentDAO, userDAO);
        studentPanel = new StudentDashboardPanel(this, studentDAO, internshipDAO);
        facultyPanel = new FacultyDashboardPanel(this, internshipDAO);
        hodPanel = new HodDashboardPanel(this, internshipDAO);

        contentCards.add(loginPanel, "LOGIN");
        contentCards.add(adminPanel, "ADMIN");
        contentCards.add(studentPanel, "STUDENT");
        contentCards.add(facultyPanel, "FACULTY");
        contentCards.add(hodPanel, "HOD");

        mainContainer.add(contentCards, BorderLayout.CENTER);
        setContentPane(mainContainer);

        pack();
        setLocationRelativeTo(null); // Center on screen
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(Theme.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(240, 700));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER_DEFAULT));

        // Top Brand Header
        JPanel brandBox = new JPanel(new BorderLayout(0, 4));
        brandBox.setOpaque(false);
        brandBox.setBorder(new EmptyBorder(24, 20, 20, 20));

        JLabel lblLogo = new JLabel("🎓  Internship Tracker");
        lblLogo.setFont(Theme.FONT_SUBHEADER);
        lblLogo.setForeground(Theme.ACCENT_PRIMARY);

        JLabel lblSub = new JLabel("Enterprise Portal");
        lblSub.setFont(Theme.FONT_SMALL);
        lblSub.setForeground(Theme.TEXT_MUTED);

        brandBox.add(lblLogo, BorderLayout.NORTH);
        brandBox.add(lblSub, BorderLayout.SOUTH);
        sidebar.add(brandBox, BorderLayout.NORTH);

        // Middle Nav Buttons
        navButtonsBox = new JPanel();
        navButtonsBox.setLayout(new BoxLayout(navButtonsBox, BoxLayout.Y_AXIS));
        navButtonsBox.setOpaque(false);
        navButtonsBox.setBorder(new EmptyBorder(12, 12, 12, 12));
        sidebar.add(navButtonsBox, BorderLayout.CENTER);

        // Bottom User Profile & Logout Box
        JPanel bottomBox = new JPanel(new BorderLayout(0, 12));
        bottomBox.setOpaque(false);
        bottomBox.setBorder(new EmptyBorder(16, 16, 20, 16));

        CardPanel userCard = new CardPanel(8, new Insets(10, 12, 10, 12));
        userCard.setLayout(new BorderLayout(0, 2));

        lblUserFullName = new JLabel("User Name");
        lblUserFullName.setFont(Theme.FONT_BODY_BOLD);
        lblUserFullName.setForeground(Theme.TEXT_PRIMARY);

        lblUserRole = new JLabel("ROLE");
        lblUserRole.setFont(Theme.FONT_SMALL);
        lblUserRole.setForeground(Theme.ACCENT_PRIMARY);

        userCard.add(lblUserFullName, BorderLayout.NORTH);
        userCard.add(lblUserRole, BorderLayout.SOUTH);

        StyledButton btnLogout = StyledButton.createSecondary("🚪  Log Out");
        btnLogout.setPreferredSize(new Dimension(btnLogout.getPreferredSize().width, 36));
        btnLogout.addActionListener(e -> logout());

        bottomBox.add(userCard, BorderLayout.NORTH);
        bottomBox.add(btnLogout, BorderLayout.SOUTH);
        sidebar.add(bottomBox, BorderLayout.SOUTH);

        return sidebar;
    }

    private void updateSidebarForRole(User user) {
        navButtonsBox.removeAll();
        lblUserFullName.setText(user.getFullName());
        lblUserRole.setText(user.getRole().toUpperCase());

        String role = user.getRole().toUpperCase();
        if ("ADMIN".equals(role)) {
            StyledButton btn = StyledButton.createSidebarNav("👥  Student Directory");
            btn.setNavActive(true);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            navButtonsBox.add(btn);
        } else if ("STUDENT".equals(role)) {
            StyledButton btn = StyledButton.createSidebarNav("📝  My Internship Portal");
            btn.setNavActive(true);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            navButtonsBox.add(btn);
        } else if ("FACULTY".equals(role)) {
            StyledButton btn = StyledButton.createSidebarNav("📋  Reviews & Approvals");
            btn.setNavActive(true);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            navButtonsBox.add(btn);
        } else if ("HOD".equals(role)) {
            StyledButton btn = StyledButton.createSidebarNav("📊  Department Analytics");
            btn.setNavActive(true);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            navButtonsBox.add(btn);
        }

        navButtonsBox.revalidate();
        navButtonsBox.repaint();
    }

    public void onLoginSuccess(User user) {
        this.currentUser = user;
        updateSidebarForRole(user);
        sidebarPanel.setVisible(true);

        String role = user.getRole().toUpperCase();
        switch (role) {
            case "ADMIN":
                adminPanel.setUser(user);
                cardLayout.show(contentCards, "ADMIN");
                break;
            case "STUDENT":
                studentPanel.setUser(user);
                cardLayout.show(contentCards, "STUDENT");
                break;
            case "FACULTY":
                facultyPanel.setUser(user);
                cardLayout.show(contentCards, "FACULTY");
                break;
            case "HOD":
                hodPanel.setUser(user);
                cardLayout.show(contentCards, "HOD");
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown role: " + role, "Error", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    public void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out of your session?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            this.currentUser = null;
            sidebarPanel.setVisible(false);
            loginPanel.resetForm();
            cardLayout.show(contentCards, "LOGIN");
        }
    }
}
