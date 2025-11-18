package ui;

import javax.swing.table.AbstractTableModel;
import structures.Task;
import structures.TaskList;

public class TaskTableModel extends AbstractTableModel {

    private TaskList taskList;

    private final String[] columns = {
        "Title",
        "Priority",
        "Deadline",
        "Actions" // Column for action buttons
    };

    public TaskTableModel(TaskList list) {
        this.taskList = list;
    }
    
    @Override
    public int getRowCount() {
        return taskList.size(); 
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = taskList.get(rowIndex);

        switch (columnIndex) {
            case 0: return task.getTitle();
            case 1: return task.getPriority().name(); // Showing Priority as String
            case 2: return task.getDeadline(); // Assuming it's a String or formatted date
            case 3: return "⋯"; // Placeholder for actions
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: return String.class; // Title
            case 1: return String.class; // Priority
            case 2: return String.class; // Deadline
            case 3: return Object.class; // Actions (button)
            default: return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Only the Actions column should be editable for button rendering
        return columnIndex == 3;
    }

    public Task getTaskAt(int row) {
        return taskList.get(row);
    }

    public boolean markTaskComplete(int taskId) {
        boolean success = taskList.markComplete(taskId);
        if (success) {
            fireTableDataChanged();
        }
        return success;
    }

    public boolean deleteTask(int taskId) {
        boolean success = taskList.deleteTask(taskId);
        if (success) {
            fireTableDataChanged();
        }
        return success;
    }

    public void refresh() {
        fireTableDataChanged();
    }
}