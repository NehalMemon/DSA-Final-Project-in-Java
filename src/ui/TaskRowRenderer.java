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
        setHorizontalAlignment(LEFT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (!isSelected) {
            Task task = model.getTaskAt(row);
            if (task != null) {
                switch (task.getPriority()) {
                    case high:
                        c.setBackground(new Color(255, 204, 204)); // light red
                        break;
                    case medium:
                        c.setBackground(new Color(255, 255, 204)); // light yellow
                        break;
                    case low:
                        c.setBackground(new Color(204, 255, 204)); // light green
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                }
            } else {
                c.setBackground(Color.WHITE);
            }
        } else {
            c.setBackground(table.getSelectionBackground());
        }

        return c;
    }
}
