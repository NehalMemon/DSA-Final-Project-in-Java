package ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TaskActionRenderer extends JPanel implements TableCellRenderer {

    // Theme Colors for Buttons (Use standard colors for contrast)
    private final Color DETAILS_COLOR = new Color(0x3498db); // Blue
    private final Color COMPLETE_COLOR = new Color(0x2ecc71); // Green (SUCCESS)
    private final Color TODAY_COLOR = new Color(0xf1c40f);    // Yellow (WARNING)
    private final Color DELETE_COLOR = new Color(0xe74c3c);    // Red

    private final JButton detailButton;
    private final JButton completeButton;
    private final JButton todayButton;
    private final JButton deleteButton;

    // Use a compatible font size for simple symbols
    private static final Font SYMBOL_FONT = new Font("Dialog", Font.BOLD, 12); 

    public TaskActionRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 6, 2));
        setOpaque(true);
        
        // --- Create Styled Buttons using simple Unicode symbols ---
        // Using characters similar to the ones you confirmed are working
        detailButton = makeIconButton("►", "View Details", DETAILS_COLOR); // ►
        completeButton = makeIconButton("✓", "Mark Complete", COMPLETE_COLOR); // ✓
        todayButton = makeIconButton("★", "Add to Today", TODAY_COLOR); // ★
        deleteButton = makeIconButton("✗", "Delete Task", DELETE_COLOR); // ✕ (Simplified X, similar to ✗)

        add(detailButton);
        add(completeButton);
        add(todayButton);
        add(deleteButton);
    }

    private JButton makeIconButton(String text, String tooltip, Color bgColor) {
        JButton b = new JButton(text);
        b.setToolTipText(tooltip);
        b.setFocusable(false);
        b.setPreferredSize(new Dimension(34, 28));
        
        // Apply distinct color and ensure visibility
        b.setBackground(bgColor);
        b.setForeground(Color.WHITE);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Use the SYMBOL_FONT
        b.setFont(SYMBOL_FONT); 
        return b;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        
        // 1. Get the background color set by the TaskRowRenderer for this row
        Component rowRenderer = table.getDefaultRenderer(Object.class)
                .getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // 2. Set THIS panel's background to match the row background
        this.setBackground(rowRenderer.getBackground());
        
        // (Re-)Ensure buttons have the correct background
        detailButton.setBackground(DETAILS_COLOR);
        completeButton.setBackground(COMPLETE_COLOR);
        todayButton.setBackground(TODAY_COLOR);
        deleteButton.setBackground(DELETE_COLOR);
        
        return this;
    }
}