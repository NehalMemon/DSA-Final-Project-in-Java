package ui;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import structures.Task;
import structures.TaskStack;

/**
 * Table Model for CompletedTasks UI.
 * Works seamlessly with the professional-themed JTable.
 */
public class CompletedTasksTableModel extends AbstractTableModel {
    private TaskStack stack;
    private List<Task> currentTasks; 
    private final String[] columnNames = {"ID", "Task Title", "Due Date", "Action"};

    public CompletedTasksTableModel(TaskStack stack) {
        this.stack = stack;
        this.currentTasks = stack.getTasks();
    }

    /**
     * Refreshes the internal data from the stack and notifies the table.
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
        Task task = currentTasks.get(rowIndex);
        switch (columnIndex) {
            case 0: return task.getId();
            case 1: return task.getTitle();
            case 2: return task.getDeadline();
            case 3: return "Undo"; // Will be rendered as a professional button
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return Integer.class; // ID
            case 3: return String.class;  // Action button
            default: return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3; // Only the Undo button column is editable/clickable
    }
}
