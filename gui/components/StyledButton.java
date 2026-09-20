import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Reusable Modern Rounded Button with smooth hover and pressed states.
 */
public class StyledButton extends JButton {
    private Color defaultBg;
    private Color hoverBg;
    private Color pressedBg;
    private Color defaultFg;
    private int cornerRadius = 8;
    private boolean isSidebarNav = false;
    private boolean isNavActive = false;

    public StyledButton(String text, Color bg, Color hover, Color fg) {
        super(text);
        this.defaultBg = bg;
        this.hoverBg = hover;
        this.pressedBg = hover.darker();
        this.defaultFg = fg;

        setFont(Theme.FONT_BODY_BOLD);
        setForeground(defaultFg);
        setBackground(defaultBg);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(new EmptyBorder(8, 16, 8, 16));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled() && !isNavActive) {
                    setBackground(hoverBg);
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (isEnabled() && !isNavActive) {
                    setBackground(defaultBg);
                    repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isEnabled()) {
                    setBackground(pressedBg);
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isEnabled()) {
                    setBackground(isNavActive ? defaultBg : hoverBg);
                    repaint();
                }
            }
        });
    }

    public static StyledButton createPrimary(String text) {
        return new StyledButton(text, Theme.ACCENT_PRIMARY, Theme.ACCENT_HOVER, Theme.TEXT_WHITE);
    }

    public static StyledButton createSecondary(String text) {
        StyledButton btn = new StyledButton(text, Theme.ACCENT_SECONDARY, Theme.ACCENT_SECONDARY.brighter(), Theme.TEXT_PRIMARY);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER_DEFAULT, 1, true),
                new EmptyBorder(7, 15, 7, 15)
        ));
        return btn;
    }

    public static StyledButton createSuccess(String text) {
        return new StyledButton(text, Theme.SUCCESS, Theme.SUCCESS_HOVER, Theme.TEXT_WHITE);
    }

    public static StyledButton createDanger(String text) {
        return new StyledButton(text, Theme.DANGER, Theme.DANGER_HOVER, Theme.TEXT_WHITE);
    }

    public static StyledButton createSidebarNav(String text) {
        StyledButton btn = new StyledButton(text, Color.decode("#0B0F19"), new Color(0x1E, 0x29, 0x3B), Theme.TEXT_SECONDARY);
        btn.setFont(Theme.FONT_BODY);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 16, 10, 16));
        btn.isSidebarNav = true;
        return btn;
    }

    public void setNavActive(boolean active) {
        this.isNavActive = active;
        if (active) {
            this.defaultBg = Theme.BG_CARD;
            this.defaultFg = Theme.ACCENT_PRIMARY;
            setFont(Theme.FONT_BODY_BOLD);
            setBackground(defaultBg);
            setForeground(defaultFg);
        } else {
            this.defaultBg = Color.decode("#0B0F19");
            this.defaultFg = Theme.TEXT_SECONDARY;
            setFont(Theme.FONT_BODY);
            setBackground(defaultBg);
            setForeground(defaultFg);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // If sidebar active, draw left accent bar
        if (isSidebarNav && isNavActive) {
            g2.setColor(Theme.ACCENT_PRIMARY);
            g2.fillRoundRect(0, 4, 4, getHeight() - 8, 4, 4);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
