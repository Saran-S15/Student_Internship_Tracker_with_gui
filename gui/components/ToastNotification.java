import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Inline feedback toast banner component for non-blocking notifications.
 */
public class ToastNotification extends JPanel {
    private final JLabel lblMessage;
    private Color bannerBg;
    private Color bannerBorder;

    public ToastNotification() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setVisible(false);
        setBorder(new EmptyBorder(8, 14, 8, 14));

        lblMessage = new JLabel("");
        lblMessage.setFont(Theme.FONT_BODY);
        lblMessage.setHorizontalAlignment(SwingConstants.LEFT);
        add(lblMessage, BorderLayout.CENTER);
    }

    public void showSuccess(String msg) {
        lblMessage.setText("✓  " + msg);
        lblMessage.setForeground(new Color(0xA7, 0xF3, 0xD0));
        this.bannerBg = new Color(0x06, 0x4E, 0x3B);
        this.bannerBorder = new Color(0x04, 0x78, 0x57);
        setVisible(true);
        repaint();
    }

    public void showError(String msg) {
        lblMessage.setText("⚠  " + msg);
        lblMessage.setForeground(new Color(0xFE, 0xCA, 0xCA));
        this.bannerBg = new Color(0x7F, 0x1D, 0x1D);
        this.bannerBorder = new Color(0xB9, 0x1C, 0x1C);
        setVisible(true);
        repaint();
    }

    public void showInfo(String msg) {
        lblMessage.setText("ℹ  " + msg);
        lblMessage.setForeground(new Color(0xCC, 0xFB, 0xF1));
        this.bannerBg = new Color(0x13, 0x4E, 0x48);
        this.bannerBorder = new Color(0x0D, 0x94, 0x88);
        setVisible(true);
        repaint();
    }

    public void hideToast() {
        setVisible(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!isVisible() || bannerBg == null) return;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(bannerBg);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

        g2.setColor(bannerBorder);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

        g2.dispose();
        super.paintComponent(g);
    }
}
