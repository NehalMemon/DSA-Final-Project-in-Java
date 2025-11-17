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

    // Constructor to receive the data list
    public TaskTableModel(TaskList list) {
        this.taskList = list;
    }
    
    // --- AbstractTableModel Implementation ---
    
    @Override
    public int getRowCount() {
        // FIX: Returns the size of the underlying linked list
        return taskList.size(); 
    }

    @Override
    public int getColumnCount() {
        // FIX: Returns the number of columns defined
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // Retrieve the Task object using the row index
        Task task = taskList.get(rowIndex);

        switch (columnIndex) {
            case 0: return task.getTitle();
            case 1: return task.getPriority().name();
            case 2: return task.getDeadline();
            case 3: return "⋯"; // Placeholder for the action buttons
        }
        return null;
    }
    
    // --- Custom Methods for UI/Actions ---

    // Used by Action Renderer/Editor to get the task object
    public Task getTaskAt(int row) {
        return taskList.get(row);
    }
    
    /** Action: Marks a task complete and refreshes the table. */
    public boolean markTaskComplete(int taskId) {
        boolean success = taskList.markComplete(taskId); // Calls the TaskList structure
        if (success) {
            fireTableDataChanged(); // Notify the JTable to redraw
        }
        return success;
    }

    /** Action: Deletes a task and refreshes the table. */
    public boolean deleteTask(int taskId) {
        boolean success = taskList.deleteTask(taskId); // Calls the TaskList structure
        if (success) {
            fireTableDataChanged(); // Notify the JTable to redraw (will reduce row count)
        }
        return success;
    }

    public void refresh() {
        fireTableDataChanged();
    }
}