import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Metric Card Component for Dashboard KPI statistics.
 */
public class StatCard extends JPanel {
    private final JLabel lblTitle;
    private final JLabel lblValue;
    private final JLabel lblSubtext;
    private final Color cardBg;
    private final Color valueColor;

    public StatCard(String title, String initialValue, Color valueColor, Color cardBg) {
        this.valueColor = valueColor;
        this.cardBg = cardBg;

        setOpaque(false);
        setLayout(new BorderLayout(0, 6));
        setBorder(new EmptyBorder(16, 18, 16, 18));

        lblTitle = new JLabel(title.toUpperCase());
        lblTitle.setFont(Theme.FONT_SMALL);
        lblTitle.setForeground(Theme.TEXT_SECONDARY);

        lblValue = new JLabel(initialValue);
        lblValue.setFont(Theme.FONT_STAT_NUM);
        lblValue.setForeground(valueColor);

        lblSubtext = new JLabel("");
        lblSubtext.setFont(Theme.FONT_SMALL);
        lblSubtext.setForeground(Theme.TEXT_MUTED);

        JPanel centerBox = new JPanel(new BorderLayout(0, 2));
        centerBox.setOpaque(false);
        centerBox.add(lblValue, BorderLayout.CENTER);
        centerBox.add(lblSubtext, BorderLayout.SOUTH);

        add(lblTitle, BorderLayout.NORTH);
        add(centerBox, BorderLayout.CENTER);
    }

    public void setValue(String value) {
        lblValue.setText(value);
    }

    public void setSubtext(String subtext) {
        lblSubtext.setText(subtext);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill background
        g2.setColor(cardBg);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

        // Draw border
        g2.setColor(Theme.BORDER_DEFAULT);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);

        g2.dispose();
        super.paintComponent(g);
    }
}
