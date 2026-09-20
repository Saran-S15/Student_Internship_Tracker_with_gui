import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Modern FlatLaf Modal Dialog for entering Faculty Rejection Remarks.
 */
public class RemarkModalDialog extends JDialog {
    private final JTextArea taRemark;
    private final ToastNotification toast;
    private String resultRemark = null;

    public RemarkModalDialog(Frame owner, String studentName, String company) {
        super(owner, "Rejection Reason (Required)", true);

        JPanel contentPane = new JPanel(new BorderLayout(0, 14));
        contentPane.setBackground(Theme.BG_APP);
        contentPane.setBorder(new EmptyBorder(20, 24, 20, 24));

        toast = new ToastNotification();
        contentPane.add(toast, BorderLayout.NORTH);

        CardPanel card = new CardPanel(10, new Insets(16, 16, 16, 16));
        card.setLayout(new BorderLayout(0, 10));

        JLabel lblPrompt = new JLabel("<html>Please provide a rejection reason for <b>" + studentName + "</b> (" + company + "):</html>");
        lblPrompt.setFont(Theme.FONT_BODY);
        lblPrompt.setForeground(Theme.TEXT_PRIMARY);

        taRemark = new JTextArea(4, 25);
        taRemark.setFont(Theme.FONT_BODY);
        taRemark.setForeground(Theme.TEXT_PRIMARY);
        taRemark.setBackground(Theme.BG_INPUT);
        taRemark.setCaretColor(Theme.ACCENT_PRIMARY);
        taRemark.setLineWrap(true);
        taRemark.setWrapStyleWord(true);
        taRemark.setBorder(new EmptyBorder(8, 10, 8, 10));

        JScrollPane scroll = new JScrollPane(taRemark);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER_DEFAULT, 1, true));
        scroll.getViewport().setBackground(Theme.BG_INPUT);

        card.add(lblPrompt, BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        contentPane.add(card, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        StyledButton btnCancel = StyledButton.createSecondary("Cancel");
        btnCancel.addActionListener(e -> dispose());

        StyledButton btnConfirm = StyledButton.createDanger("Confirm Rejection");
        btnConfirm.addActionListener(e -> {
            String text = taRemark.getText().trim();
            if (text.isEmpty()) {
                toast.showError("Rejection reason is required and cannot be empty.");
            } else {
                resultRemark = text;
                dispose();
            }
        });

        btnPanel.add(btnCancel);
        btnPanel.add(btnConfirm);
        contentPane.add(btnPanel, BorderLayout.SOUTH);

        setContentPane(contentPane);
        setSize(460, 320);
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    public String showDialog() {
        setVisible(true);
        return resultRemark;
    }
}
