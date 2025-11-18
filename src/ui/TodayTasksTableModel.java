package ui;

import structures.Task;
import structures.TaskQueue;
import javax.swing.table.AbstractTableModel;

/**
 * Table Model for Today's Tasks (Queue).
 * Displays tasks in FIFO order with position numbers.
 */
public class TodayTasksTableModel extends AbstractTableModel {
    private TaskQueue queue;
    private Task[] currentTasks;
    private final String[] columnNames = {"Position", "ID", "Task Title", "Priority", "Due Date", "Actions"};

    public TodayTasksTableModel(TaskQueue queue) {
        this.queue = queue;
        this.currentTasks = queue.getAllTasks();
    }

    /**
     * Refreshes the internal task array from the queue and notifies the table.
     */
    public void refresh() {
        this.currentTasks = queue.getAllTasks();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return currentTasks.length;
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
        if (rowIndex >= currentTasks.length) {
            return null;
        }
        
        Task task = currentTasks[rowIndex];
        
        switch (columnIndex) {
            case 0: return "#" + (rowIndex + 1); // Position in queue
            case 1: return task.getId();
            case 2: return task.getTitle();
            case 3: return task.getPriority().name();
            case 4: return task.getDeadline();
            case 5: return "Actions"; // Placeholder for buttons
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0 || columnIndex == 5) {
            return String.class;
        }
        if (columnIndex == 1) {
            return Integer.class;
        }
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Only the Actions column (index 5) is editable/clickable
        return columnIndex == 5;
    }

    /**
     * Get the task at a specific row (for button actions)
     */
    public Task getTaskAt(int row) {
        if (row >= 0 && row < currentTasks.length) {
            return currentTasks[row];
        }
        return null;
    }
}