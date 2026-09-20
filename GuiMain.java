import javax.swing.*;

/**
 * Main Entry Point for the Desktop GUI version of Student Internship Tracker.
 */
public class GuiMain {
    public static void main(String[] args) {
        // Initialize FlatDarkLaf theme and antialiased rendering
        Theme.initializeLookAndFeel();

        // Launch GUI on Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
