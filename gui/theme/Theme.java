import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Centralized Design System & Theme Configuration for Student Internship Tracker.
 * Manages FlatDarkLaf setup, color palette, typography, and reusable UI styles.
 */
public class Theme {

    // ==========================================
    // COLOR PALETTE (Deep Slate / Navy & Teal)
    // ==========================================
    public static final Color BG_APP          = new Color(0x0F, 0x17, 0x2A); // Main App Canvas (#0F172A)
    public static final Color BG_CARD         = new Color(0x1E, 0x29, 0x3B); // Elevated Surface Card (#1E293B)
    public static final Color BG_SIDEBAR      = new Color(0x0B, 0x0F, 0x19); // Deep Dark Sidebar (#0B0F19)
    public static final Color BG_INPUT        = new Color(0x0F, 0x17, 0x2A); // Textfield / Input Fill
    public static final Color BG_TABLE_HEADER = new Color(0x16, 0x20, 0x32); // Table Column Header
    public static final Color BG_TABLE_ROW_ALT= new Color(0x1B, 0x24, 0x36); // Alternating Table Row
    public static final Color BG_SELECTION    = new Color(0x13, 0x4E, 0x48); // Table & List Selection

    // Accent Colors
    public static final Color ACCENT_PRIMARY  = new Color(0x0D, 0x94, 0x88); // Teal 600 (#0D9488)
    public static final Color ACCENT_HOVER    = new Color(0x14, 0xB8, 0xA6); // Teal 500 (#14B8A6)
    public static final Color ACCENT_INDIGO   = new Color(0x63, 0x66, 0xF1); // Indigo 500 (#6366F1)
    public static final Color ACCENT_SECONDARY= new Color(0x33, 0x41, 0x55); // Slate 700 (#334155)

    // Functional State Colors
    public static final Color SUCCESS         = new Color(0x10, 0xB9, 0x81); // Emerald Green
    public static final Color SUCCESS_HOVER   = new Color(0x05, 0x96, 0x69);
    public static final Color DANGER          = new Color(0xEF, 0x44, 0x44); // Ruby Red
    public static final Color DANGER_HOVER    = new Color(0xDC, 0x26, 0x26);
    public static final Color WARNING         = new Color(0xF5, 0x9E, 0x0B); // Amber Yellow

    // Text Colors
    public static final Color TEXT_PRIMARY    = new Color(0xF8, 0xFA, 0xFC); // Slate 50 (#F8FAFC)
    public static final Color TEXT_SECONDARY  = new Color(0x94, 0xA3, 0xB8); // Slate 400 (#94A3B8)
    public static final Color TEXT_MUTED      = new Color(0x64, 0x74, 0x8B); // Slate 500 (#64748B)
    public static final Color TEXT_WHITE      = Color.WHITE;

    // Borders & Dividers
    public static final Color BORDER_DEFAULT  = new Color(0x33, 0x41, 0x55); // Slate 700 (#334155)
    public static final Color BORDER_FOCUS    = new Color(0x0D, 0x94, 0x88); // Teal focus outline

    // Status Badges
    public static final Color BADGE_PENDING_BG  = new Color(0x78, 0x35, 0x0F); // Amber Dark
    public static final Color BADGE_PENDING_TEXT= new Color(0xFD, 0xE6, 0x8A); // Amber Light
    public static final Color BADGE_PENDING_BD  = new Color(0x92, 0x40, 0x0E);

    public static final Color BADGE_APPROVED_BG = new Color(0x06, 0x4E, 0x3B); // Emerald Dark
    public static final Color BADGE_APPROVED_TEXT=new Color(0xA7, 0xF3, 0xD0); // Emerald Light
    public static final Color BADGE_APPROVED_BD = new Color(0x04, 0x78, 0x57);

    public static final Color BADGE_REJECTED_BG = new Color(0x7F, 0x1D, 0x1D); // Red Dark
    public static final Color BADGE_REJECTED_TEXT=new Color(0xFE, 0xCA, 0xCA); // Red Light
    public static final Color BADGE_REJECTED_BD = new Color(0xB9, 0x1C, 0x1C);

    // ==========================================
    // TYPOGRAPHY (Segoe UI / Inter Hierarchy)
    // ==========================================
    public static final Font FONT_APP_TITLE  = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_PAGE_HEADER= new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBHEADER  = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY_BOLD  = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_BODY       = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL      = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BADGE      = new Font("Segoe UI", Font.BOLD, 11);
    public static final Font FONT_STAT_NUM   = new Font("Segoe UI", Font.BOLD, 28);

    /**
     * Initializes FlatDarkLaf and configures global look and feel properties.
     */
    public static void initializeLookAndFeel() {
        try {
            // Setup FlatDarkLaf
            FlatDarkLaf.setup();

            // Customize UI Defaults for rounded modern look
            UIManager.put("Button.arc", 8);
            UIManager.put("Component.arc", 8);
            UIManager.put("TextComponent.arc", 8);
            UIManager.put("ProgressBar.arc", 8);
            UIManager.put("ScrollBar.thumbArc", 6);
            UIManager.put("ScrollBar.width", 10);

            UIManager.put("Panel.background", BG_APP);
            UIManager.put("Viewport.background", BG_CARD);
            UIManager.put("ScrollPane.background", BG_CARD);

            // Antialiased text
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");

        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf theme: " + e.getMessage());
        }
    }

    /**
     * Creates an EmptyBorder with standard spacing.
     */
    public static Border createPadding(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }
}
