import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Centered Card Login Panel with role auto-detection and inline error feedback.
 */
public class LoginPanel extends JPanel {
    private final MainFrame mainFrame;
    private final UserDAO userDAO;

    private StyledTextField tfUsername;
    private JPasswordField pfPassword;
    private ToastNotification toast;
    private StyledButton btnLogin;

    public LoginPanel(MainFrame mainFrame, UserDAO userDAO) {
        this.mainFrame = mainFrame;
        this.userDAO = userDAO;
        buildUI();
    }

    private void buildUI() {
        setLayout(new GridBagLayout());
        setBackground(Theme.BG_APP);

        CardPanel card = new CardPanel(16, new Insets(36, 36, 36, 36));
        card.setPreferredSize(new Dimension(420, 500));
        card.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // 1. App Icon & Title Header
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 4, 0);
        JLabel lblTitle = new JLabel("Internship Tracker", SwingConstants.CENTER);
        lblTitle.setFont(Theme.FONT_APP_TITLE);
        lblTitle.setForeground(Theme.ACCENT_PRIMARY);
        card.add(lblTitle, gbc);

        // 2. Subtitle
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel lblSub = new JLabel("Sign in to your account", SwingConstants.CENTER);
        lblSub.setFont(Theme.FONT_BODY);
        lblSub.setForeground(Theme.TEXT_SECONDARY);
        card.add(lblSub, gbc);

        // 3. Inline Toast Error Banner
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 16, 0);
        toast = new ToastNotification();
        card.add(toast, gbc);

        // 4. Username Field
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 4, 0);
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(Theme.FONT_BODY_BOLD);
        lblUser.setForeground(Theme.TEXT_SECONDARY);
        card.add(lblUser, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 14, 0);
        tfUsername = new StyledTextField(20);
        card.add(tfUsername, gbc);

        // 5. Password Field
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 4, 0);
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(Theme.FONT_BODY_BOLD);
        lblPass.setForeground(Theme.TEXT_SECONDARY);
        card.add(lblPass, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 24, 0);
        pfPassword = new JPasswordField(20);
        pfPassword.setFont(Theme.FONT_BODY);
        pfPassword.setForeground(Theme.TEXT_PRIMARY);
        pfPassword.setBackground(Theme.BG_INPUT);
        pfPassword.setCaretColor(Theme.ACCENT_PRIMARY);
        pfPassword.setBorder(new EmptyBorder(8, 12, 8, 12));
        pfPassword.setPreferredSize(new Dimension(pfPassword.getPreferredSize().width, 38));
        card.add(pfPassword, gbc);

        // 6. Sign In Button
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 0, 0);
        btnLogin = StyledButton.createPrimary("Sign In");
        btnLogin.setPreferredSize(new Dimension(btnLogin.getPreferredSize().width, 42));
        btnLogin.addActionListener(e -> handleLogin());
        card.add(btnLogin, gbc);

        // Enter Key Listener
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };
        tfUsername.addKeyListener(enterListener);
        pfPassword.addKeyListener(enterListener);

        add(card);
    }

    public void resetForm() {
        tfUsername.setText("");
        pfPassword.setText("");
        toast.hideToast();
        tfUsername.requestFocusInWindow();
    }

    private void handleLogin() {
        String username = tfUsername.getText().trim();
        String password = new String(pfPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            toast.showError("Please enter both username and password.");
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Authenticating...");

        // Non-blocking SwingWorker for database authentication
        SwingWorker<User, Void> worker = new SwingWorker<>() {
            @Override
            protected User doInBackground() throws Exception {
                return userDAO.authenticate(username, password);
            }

            @Override
            protected void done() {
                btnLogin.setEnabled(true);
                btnLogin.setText("Sign In");
                try {
                    User user = get();
                    if (user != null) {
                        toast.hideToast();
                        mainFrame.onLoginSuccess(user);
                    }
                } catch (Exception e) {
                    Throwable cause = e.getCause() != null ? e.getCause() : e;
                    toast.showError(cause.getMessage());
                }
            }
        };
        worker.execute();
    }
}
