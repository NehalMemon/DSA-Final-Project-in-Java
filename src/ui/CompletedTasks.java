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

    // Theme Colors
    private final Color BG_DARK = new Color(0x3d3b3c);
    private final Color ACCENT = new Color(0xb592a0);
    private final Color ACCENT2 = new Color(0x7a9e9f);
    private final Color SUCCESS = new Color(0x6bffb8);
    private final Color WARNING = new Color(0xbc5f04);
    
    // Derived Colors
    private final Color TABLE_ROW_BG = new Color(0x2f2f2f); // Slightly lighter dark than BG_DARK
    private final Color TABLE_HEADER_BG = ACCENT2; // Using ACCENT2 for the table header
    private final Color UNDO_BUTTON_BG = WARNING; // Using WARNING for the Undo button

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

        // --- Styles matching MainWindow ---
        // Use a consistent dark background for the frame content pane
        getContentPane().setBackground(BG_DARK); 
        
        // Using a system-wide font for consistency with MainWindow setup
        Font headerFont = new Font("Segoe UI", Font.BOLD, 18);
        Font tableFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_DARK); // Dark background
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Completed Tasks (Most Recent on Top)");
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(ACCENT); // Use ACCENT color for the title
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // *** NEW: Refresh Button ***
        JButton refreshBtn = styledButton("↻ Refresh", ACCENT2.darker(), headerFont.deriveFont(Font.BOLD, 12));
        refreshBtn.addActionListener(e -> refreshTable());
        
        JPanel rightHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightHeaderPanel.setBackground(BG_DARK);
        rightHeaderPanel.add(refreshBtn);
        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // --- Table Setup ---
        tableModel = new CompletedTasksTableModel(completedStack);
        completedTable = new JTable(tableModel);

        // Table Styling
        completedTable.setFont(tableFont);
        completedTable.setRowHeight(35);
        completedTable.setGridColor(BG_DARK.brighter()); // Grid lines slightly brighter than background
        completedTable.setBackground(TABLE_ROW_BG); // Darker background for table rows
        completedTable.setForeground(Color.WHITE);
        completedTable.setSelectionBackground(ACCENT2.darker()); // Darker ACCENT2 for selection
        completedTable.setSelectionForeground(Color.WHITE);
        completedTable.setAutoCreateRowSorter(true); 

        // Table Header Styling
        completedTable.getTableHeader().setBackground(TABLE_HEADER_BG); // Using ACCENT2
        completedTable.getTableHeader().setForeground(Color.WHITE);
        completedTable.getTableHeader().setFont(headerFont.deriveFont(Font.BOLD, 15));
        completedTable.getTableHeader().setBorder(BorderFactory.createLineBorder(BG_DARK.brighter()));

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
                // CRITICAL FIX: Restore the task to the main list
                mainTaskList.createTask(poppedTask); 
    
                // 4. Refresh the display
                tableModel.refresh();
                
                // Show confirmation message
                JOptionPane.showMessageDialog(this, 
                    "Task '" + taskToUndo.getTitle() + "' restored to main list.", 
                    "Task Undone", JOptionPane.INFORMATION_MESSAGE);
                
                // Note: The MainWindow's model should be refreshed when this window closes or by the user refreshing MainWindow.
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
        scrollPane.getViewport().setBackground(TABLE_ROW_BG); // Apply dark background
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JButton styledButton(String text, Color bg, Font font) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        b.setFocusPainted(false);
        b.setFont(font);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setBorderPainted(false);
        return b;
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
            setBackground(UNDO_BUTTON_BG); // Use WARNING color for "Undo"
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
            button.setBackground(UNDO_BUTTON_BG); // Use WARNING color for "Undo"
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