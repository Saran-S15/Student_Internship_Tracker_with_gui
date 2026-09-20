import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Modern Card Component with rounded corners and dark elevated background (#1E293B).
 */
public class CardPanel extends JPanel {
    private int cornerRadius = 12;

    public CardPanel() {
        this(12, new Insets(16, 16, 16, 16));
    }

    public CardPanel(int cornerRadius, Insets padding) {
        this.cornerRadius = cornerRadius;
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(padding));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill background
        g2.setColor(Theme.BG_CARD);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        // Draw border
        g2.setColor(Theme.BORDER_DEFAULT);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();
        super.paintComponent(g);
    }
}
