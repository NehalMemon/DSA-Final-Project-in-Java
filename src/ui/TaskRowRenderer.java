package ui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import structures.Task;

public class TaskRowRenderer extends DefaultTableCellRenderer {

    private TaskTableModel model;

    public TaskRowRenderer(TaskTableModel model) {
        this.model = model;
        // Align deadline column to the right for better reading
        setHorizontalAlignment(CENTER); 
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (!isSelected) {
            Task task = model.getTaskAt(row);
            
            // Set the background color based on priority
            switch (task.getPriority()) {
                case high:
                    c.setBackground(new Color(255, 192, 192)); // Light Red
                    break;
                case medium:
                    c.setBackground(new Color(255, 255, 192)); // Light Yellow
                    break;
                case low:
                    c.setBackground(new Color(192, 255, 192)); // Light Green
                    break;
                default:
                    c.setBackground(Color.WHITE);
            }
        } else {
            // Use default selection color
            c.setBackground(table.getSelectionBackground());
        }
        
        return c;
    }
}