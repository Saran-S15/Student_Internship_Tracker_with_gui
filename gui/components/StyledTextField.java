import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Modern Rounded Input Text Field with subtle focus glow border.
 */
public class StyledTextField extends JTextField {
    private boolean isFocused = false;
    private int cornerRadius = 8;

    public StyledTextField() {
        this(15);
    }

    public StyledTextField(int columns) {
        super(columns);
        setFont(Theme.FONT_BODY);
        setForeground(Theme.TEXT_PRIMARY);
        setBackground(Theme.BG_INPUT);
        setCaretColor(Theme.ACCENT_PRIMARY);
        setOpaque(false);
        setBorder(new EmptyBorder(8, 12, 8, 12));
        setPreferredSize(new Dimension(getPreferredSize().width, 38));

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill background
        g2.setColor(Theme.BG_INPUT);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        // Draw border (Teal on focus, slate when blurred)
        g2.setColor(isFocused ? Theme.BORDER_FOCUS : Theme.BORDER_DEFAULT);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();
        super.paintComponent(g);
    }
}
