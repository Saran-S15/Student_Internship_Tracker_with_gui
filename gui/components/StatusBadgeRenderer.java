import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Custom TableCellRenderer that renders a colorful pill badge for Status values.
 */
public class StatusBadgeRenderer extends JPanel implements TableCellRenderer {
    private final JLabel label;
    private Color badgeBg = Theme.BADGE_PENDING_BG;
    private Color badgeFg = Theme.BADGE_PENDING_TEXT;
    private Color badgeBorder = Theme.BADGE_PENDING_BD;

    public StatusBadgeRenderer() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        label = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw pill background
                g2.setColor(badgeBg);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

                // Draw pill border
                g2.setColor(badgeBorder);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

                g2.dispose();
                super.paintComponent(g);
            }
        };
        label.setFont(Theme.FONT_BADGE);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBorder(new EmptyBorder(3, 10, 3, 10));
        label.setOpaque(false);

        add(label);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        String status = (value != null) ? value.toString().trim() : "Pending";
        label.setText(status);

        if (status.equalsIgnoreCase("Approved")) {
            badgeBg = Theme.BADGE_APPROVED_BG;
            badgeFg = Theme.BADGE_APPROVED_TEXT;
            badgeBorder = Theme.BADGE_APPROVED_BD;
        } else if (status.equalsIgnoreCase("Rejected")) {
            badgeBg = Theme.BADGE_REJECTED_BG;
            badgeFg = Theme.BADGE_REJECTED_TEXT;
            badgeBorder = Theme.BADGE_REJECTED_BD;
        } else {
            badgeBg = Theme.BADGE_PENDING_BG;
            badgeFg = Theme.BADGE_PENDING_TEXT;
            badgeBorder = Theme.BADGE_PENDING_BD;
        }

        label.setForeground(badgeFg);

        if (isSelected) {
            setBackground(Theme.BG_SELECTION);
            setOpaque(true);
        } else {
            setBackground(row % 2 == 0 ? Theme.BG_CARD : Theme.BG_TABLE_ROW_ALT);
            setOpaque(true);
        }

        return this;
    }
}
