import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Modern JTable with dark aesthetic, zebra-striped rows, and styled headers.
 */
public class StyledTable extends JTable {

    public StyledTable(DefaultTableModel model) {
        super(model);
        applyStyling();
    }

    private void applyStyling() {
        setFont(Theme.FONT_BODY);
        setForeground(Theme.TEXT_PRIMARY);
        setBackground(Theme.BG_CARD);
        setRowHeight(38);
        setShowVerticalLines(false);
        setShowHorizontalLines(true);
        setGridColor(Theme.BORDER_DEFAULT);
        setSelectionBackground(Theme.BG_SELECTION);
        setSelectionForeground(Theme.TEXT_WHITE);
        setFillsViewportHeight(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Header Styling
        JTableHeader header = getTableHeader();
        header.setFont(Theme.FONT_BODY_BOLD);
        header.setForeground(Theme.TEXT_SECONDARY);
        header.setBackground(Theme.BG_TABLE_HEADER);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);

        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, row, col);
                lbl.setFont(Theme.FONT_BODY_BOLD);
                lbl.setForeground(Theme.TEXT_SECONDARY);
                lbl.setBackground(Theme.BG_TABLE_HEADER);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_DEFAULT),
                        new EmptyBorder(8, 12, 8, 12)
                ));
                return lbl;
            }
        });

        // Alternating row background renderer
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSel, boolean hasFocus, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(tbl, val, isSel, hasFocus, row, col);
                lbl.setBorder(new EmptyBorder(0, 12, 0, 12));
                if (!isSel) {
                    lbl.setBackground(row % 2 == 0 ? Theme.BG_CARD : Theme.BG_TABLE_ROW_ALT);
                    lbl.setForeground(Theme.TEXT_PRIMARY);
                } else {
                    lbl.setBackground(Theme.BG_SELECTION);
                    lbl.setForeground(Theme.TEXT_WHITE);
                }
                return lbl;
            }
        });
    }

    /**
     * Creates a styled JScrollPane containing this table.
     */
    public JScrollPane createScrollPane() {
        JScrollPane scrollPane = new JScrollPane(this);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER_DEFAULT, 1, true));
        scrollPane.getViewport().setBackground(Theme.BG_CARD);
        return scrollPane;
    }
}
