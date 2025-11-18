package ui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import structures.Task;
import structures.TaskList;
import structures.TaskStack; 
import java.util.List;

public class CompletedTasks extends JFrame {

    // Dependency injection: You must pass the main list and the completed stack
    private TaskList mainTaskList; 
    private TaskStack completedStack;
    private CompletedTasksTableModel tableModel;
    private JTable completedTable;

    public CompletedTasks(TaskList mainTaskList, TaskStack completedStack) {
        this.mainTaskList = mainTaskList;
        this.completedStack = completedStack;

        setTitle("Completed Tasks (Stack)");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // --- Styles matching MainWindow/AddTaskForm ---
        getContentPane().setBackground(new Color(20, 20, 20));
        Font headerFont = new Font("Montserrat", Font.BOLD, 18);
        Font tableFont = new Font("Montserrat", Font.PLAIN, 14);

        // Header Panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(32, 32, 32));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        JLabel titleLabel = new JLabel("Completed Tasks (Most Recent on Top)");
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(new Color(0, 122, 255));
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- Table Setup ---
        tableModel = new CompletedTasksTableModel(completedStack);
        completedTable = new JTable(tableModel);

        // Table Styling
        completedTable.setFont(tableFont);
        completedTable.setRowHeight(35);
        completedTable.setGridColor(new Color(50, 50, 50));
        completedTable.setBackground(new Color(32, 32, 32));
        completedTable.setForeground(Color.WHITE);
        completedTable.setSelectionBackground(new Color(50, 50, 50));
        completedTable.setSelectionForeground(Color.WHITE);
        completedTable.setAutoCreateRowSorter(true); 

        // Table Header Styling
        completedTable.getTableHeader().setBackground(new Color(45, 45, 45));
        completedTable.getTableHeader().setForeground(Color.WHITE);
        completedTable.getTableHeader().setFont(headerFont.deriveFont(Font.BOLD, 14));
        completedTable.getTableHeader().setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50)));

        // Set column widths
        completedTable.getColumnModel().getColumn(0).setPreferredWidth(50); 
        completedTable.getColumnModel().getColumn(1).setPreferredWidth(300); 
        completedTable.getColumnModel().getColumn(2).setPreferredWidth(120); 
        completedTable.getColumnModel().getColumn(3).setPreferredWidth(80); 

        // --- Undo Button Renderer and Editor Setup ---
        ButtonRenderer renderer = new ButtonRenderer("Undo");
        completedTable.getColumn("Action").setCellRenderer(renderer);
        
        ButtonEditor editor = new ButtonEditor(new JTextField());
        completedTable.getColumn("Action").setCellEditor(editor);
        
        // This is the action listener for the Undo button click
        editor.getButton().addActionListener(e -> {
            // Get the row that was just clicked/edited
            int viewRow = completedTable.getEditingRow();
            if (viewRow == -1) return; // Safety check
            
            // Convert view row to model row index
            int modelRow = completedTable.convertRowIndexToModel(viewRow);
            
            // Get the list of tasks currently displayed (LIFO order)
            List<Task> currentTasks = completedStack.getTasks();
            
            // Get the task based on the current display list
            Task taskToUndo = currentTasks.get(modelRow);
            
            // --- Crucial Logic: Undo a task at an arbitrary position in the stack ---
            Task poppedTask = null;
            TaskStack tempStack = new TaskStack();
            
            // 1. Pop elements from the main stack onto the temp stack until the target task is found
            while (!completedStack.isEmpty()) {
                Task current = completedStack.pop();
                if (current.getId() == taskToUndo.getId()) {
                    poppedTask = current;
                    break; // Found and removed the target task
                }
                tempStack.push(current);
            }
            
            // 2. Restore the remaining elements back to the main completedStack
            while (!tempStack.isEmpty()) {
                completedStack.push(tempStack.pop());
            }

            // 3. Process the found task
            if (poppedTask != null) {
                poppedTask.setStatus(Task.Status.pending);
                // Assuming TaskList has an insert method that maintains list order
                mainTaskList.createTask(poppedTask); 
    
                // 4. Refresh the display
                tableModel.refresh();
                
                // Show confirmation message
                JOptionPane.showMessageDialog(this, 
                    "Task '" + taskToUndo.getTitle() + "' restored to main list.", 
                    "Task Undone", JOptionPane.INFORMATION_MESSAGE);
                
                // NOTE: You must also refresh the main window's table model (TaskTableModel) 
                // in the MainWindow's action listener when this CompletedTasks window is closed/updated.
            } else {
                 JOptionPane.showMessageDialog(this, 
                    "Error: Could not find task to undo.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Tell the table to stop editing and repaint immediately
            SwingUtilities.invokeLater(() -> completedTable.repaint());
        });

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(completedTable);
        scrollPane.getViewport().setBackground(new Color(32, 32, 32));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Helper to reload the table data when a change occurs outside this class.
     */
    public void refreshTable() {
        tableModel.refresh();
    }
    
    // --- Button Renderer Class (Draws the button) ---
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            setText(text);
            setOpaque(true);
            setBackground(new Color(255, 100, 100)); // Reddish color for "Undo"
            setForeground(Color.WHITE);
            setFont(getFont().deriveFont(Font.BOLD, 12));
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    
    // --- Button Editor Class (Handles the click event) ---
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private String label;

        public ButtonEditor(JTextField textField) {
            super(textField);
            setClickCountToStart(1); 

            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(255, 100, 100)); 
            button.setForeground(Color.WHITE);
            button.setFont(button.getFont().deriveFont(Font.BOLD, 12));
            button.setFocusPainted(false);
        }
        
        public JButton getButton() {
            return this.button;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            // The actual action logic runs in the listener attached to the editor's button.
            return label;
        }
    }
}