package ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TaskActionRenderer extends JPanel implements TableCellRenderer {

    private final JButton detailButton;
    private final JButton completeButton;
    private final JButton todayButton;
    private final JButton deleteButton; // Added deleteButton

    public TaskActionRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        setOpaque(true);

        detailButton = createButton("🔍", "View Task Details");
        completeButton = createButton("✓", "Mark as Completed");
        todayButton = createButton("⭐", "Add to Today's Tasks");
        deleteButton = createButton("🗑️", "Delete Task"); // NEW BUTTON

        add(detailButton);
        add(completeButton);
        add(todayButton);
        add(deleteButton); // Add new button
    }

    private JButton createButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(30, 25)); 
        button.setFocusable(false);
        return button;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // ... (Renderer logic remains the same)
        Component c = table.getDefaultRenderer(Object.class).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            // Get background color from the main row renderer
            Color rowColor = c.getBackground();
            setBackground(rowColor);
        }
        return this;
    }
}