package ui;

import structures.Task;
import structures.TaskStack; // Import your custom stack
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Table Model for the CompletedTasks UI.
 * Displays Task Title, Due Date, and a functional "Undo" button.
 */
public class CompletedTasksTableModel extends AbstractTableModel {
    private TaskStack stack;
    private List<Task> currentTasks; // Used for easy access via List interface
    private final String[] columnNames = {"ID", "Task Title", "Due Date", "Action"};

    public CompletedTasksTableModel(TaskStack stack) {
        this.stack = stack;
        this.currentTasks = stack.getTasks();
    }

    /**
     * Refreshes the internal List data from the stack and notifies the table.
     */
    public void refresh() {
        this.currentTasks = stack.getTasks();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return currentTasks.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // Data is pulled from the display List
        Task task = currentTasks.get(rowIndex);
        switch (columnIndex) {
            case 0: return task.getId();
            case 1: return task.getTitle();
            case 2: return task.getDeadline(); 
            case 3: return "Undo"; // Text for the button
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // The last column is always a button (rendered as String)
        if (columnIndex == 3) {
            return String.class;
        }
        if (columnIndex == 0) {
            return Integer.class; // ID column
        }
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Only the "Action" column (Undo button) is editable/clickable
        return columnIndex == 3;
    }
}