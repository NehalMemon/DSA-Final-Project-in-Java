package ui;

import javax.swing.table.AbstractTableModel;
import structures.Task;
import structures.TaskList;
import structures.TaskQueue;
import structures.TaskStack;

public class TaskTableModel extends AbstractTableModel {

    private TaskList taskList;
    private TaskQueue todayQueue;
    private TaskStack completedStack;

    private final String[] columns = {
        "Title",
        "Priority",
        "Deadline",
        "Actions"
    };

    public TaskTableModel(TaskList list, TaskQueue todayQueue, TaskStack completedStack) {
        this.taskList = list;
        this.todayQueue = todayQueue;
        this.completedStack = completedStack;
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

    /**
     * Important: provide column class so JTable can choose renderers/editors correctly.
     * - Title/Priority/Deadline are Strings
     * - Actions is Object (custom renderer/editor)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 3) return Object.class;
        return String.class;
    }

    /**
     * Critical: make only the Actions column editable so the cell editor (buttons)
     * can start when the user clicks the actions cell.
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 3; // only the Actions column is editable
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task task = taskList.get(rowIndex);

        switch (columnIndex) {
            case 0: return task.getTitle();
            case 1: return task.getPriority().name();
            case 2: return task.getDeadline();
            case 3: return "ACTIONS"; // placeholder; renderer/editor will show buttons
        }
        return null;
    }

    public Task getTaskAt(int row) {
        return taskList.get(row);
    }

    public void refresh() {
        fireTableDataChanged();
    }

    // MARK COMPLETE: marks complete, pushes onto TaskStack, AND CRITICALLY DELETES from TaskList
    public boolean markTaskComplete(int taskId) {
        Task t = taskList.findTask(taskId);
        if (t == null) return false;

        // 1. Mark complete in TaskList and capture result
        boolean success = taskList.markComplete(taskId);

        if (success && completedStack != null) {
            // 2. Push onto completed stack (using the updated task object t)
            completedStack.push(t);
            
            // 3. CRITICAL FIX: Delete the task from the main TaskList after moving it
            taskList.deleteTask(taskId); 
        }
        fireTableDataChanged();
        return success;
    }

    // ADD TO TODAY: enqueue into TaskQueue AND CRITICALLY DELETES from TaskList
    public boolean addToTodayQueue(Task task) {
        if (todayQueue == null || task == null) return false;
        
        // Check if task is already present in the queue
        if (todayQueue.findTask(task.getId()) != null) {
            return false; // already present
        }

        // 1. Enqueue into TaskQueue
        boolean ok = todayQueue.enqueue(task);

        if (ok) {
            // 2. CRITICAL FIX: Delete the task from the main TaskList after moving it
            taskList.deleteTask(task.getId());
        }

        fireTableDataChanged();
        return ok;
    }

    // DELETE TASK: delete from TaskList and remove from today queue if exists
    // NOTE: This method already correctly deletes from the main list.
    public boolean deleteTask(int taskId) {
        boolean removedFromQueue = (todayQueue != null) && todayQueue.removeTask(taskId);
        boolean success = taskList.deleteTask(taskId);
        if (success) {
            fireTableDataChanged();
        }
        return success;
    }

    // Sort helpers: call underlying TaskList sorting then refresh
    public void sortByPriority() {
        if (taskList instanceof structures.SortingAndSearch) {
            ((structures.SortingAndSearch) taskList).sortByPriority();
            fireTableDataChanged();
        }
    }

    public void sortByDeadline() {
        if (taskList instanceof structures.SortingAndSearch) {
            ((structures.SortingAndSearch) taskList).sortByDeadline();
            fireTableDataChanged();
        }
    }
}