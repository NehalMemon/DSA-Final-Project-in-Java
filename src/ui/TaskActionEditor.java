package ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import structures.Task;
import java.awt.Point; // Import for Point

public class TaskActionEditor extends DefaultCellEditor {

    // Theme Colors for Buttons (Must match Renderer)
    private final Color DETAILS_COLOR = new Color(0x3498db);
    private final Color COMPLETE_COLOR = new Color(0x2ecc71);
    private final Color TODAY_COLOR = new Color(0xf1c40f);
    private final Color DELETE_COLOR = new Color(0xe74c3c);
    
    private JPanel panel;
    private JButton detailButton, completeButton, todayButton, deleteButton;
    private TaskTableModel model;
    private JTable table;
    private Task currentTask;

    // Use a compatible font size for simple symbols
    private static final Font ICON_FONT = new Font("Dialog", Font.BOLD, 8);
    
    // Store the button that was clicked
    private JButton currentButton; 

    public TaskActionEditor(TaskTableModel model, JTable table) {
        // We use an empty JTextField here, but its properties are irrelevant now
        super(new JTextField());

        this.model = model;
        this.table = table;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 2));
        panel.setOpaque(true);
        
        // --- Create Styled Buttons with simple Unicode symbols ---
        // Note: Listeners are added here to ensure the action is performed, 
        // but the activation is handled by the manual click below.
        detailButton = createButton("►", "View Details", e -> showDetails(), DETAILS_COLOR);
        completeButton = createButton("✓", "Mark Complete", e -> markComplete(), COMPLETE_COLOR);
        todayButton = createButton("★", "Add to Today", e -> addToToday(), TODAY_COLOR);
        deleteButton = createButton("✗", "Delete", e -> deleteTask(), DELETE_COLOR);

        panel.add(detailButton);
        panel.add(completeButton);
        panel.add(todayButton);
        panel.add(deleteButton);
    }

    private JButton createButton(String text, String tooltip, ActionListener listener, Color bgColor) {
        JButton b = new JButton(text);
        b.setToolTipText(tooltip);
        b.setPreferredSize(new Dimension(34, 28));
        b.setFocusable(false);
        b.setFont(ICON_FONT); 
        b.addActionListener(listener); // Keep the listener to handle the action logic
        
        // Apply distinct color and ensure visibility
        b.setBackground(bgColor);
        b.setForeground(Color.WHITE);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(java.awt.Cursor.HAND_CURSOR));
        
        return b;
    }

    // *** CRITICAL FIX 1: Allow editing on a single mouse press ***
    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            // Check if it's the action column we care about
            if (table.columnAtPoint(((MouseEvent)anEvent).getPoint()) == 3) { 
                return ((MouseEvent)anEvent).getClickCount() >= 1;
            }
        }
        return false; // Only allow editing in the action column on single click
    }

    // *** CRITICAL FIX 2: Activate the button action when the editor component loads ***
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        int modelRow = table.convertRowIndexToModel(row);
        currentTask = model.getTaskAt(modelRow);

        // Get the row background from the renderer
        Component rowRenderer = table.getDefaultRenderer(Object.class)
                .getTableCellRendererComponent(table, value, isSelected, true, row, column);
        panel.setBackground(rowRenderer.getBackground());
        
        // Find which button was pressed and manually dispatch the action
        // We look for the mouse event that triggered the editor
        
        // The last mouse event is often tricky to get reliably. A common workaround
        // is to fire the action *after* the component is added/validated.
        SwingUtilities.invokeLater(() -> {
            // Get the point of the mouse event that started the editing
            Point point = table.getMousePosition();
            if (point != null) {
                // Convert table coordinates to the panel's coordinates
                Point panelPoint = SwingUtilities.convertPoint(table, point, panel);
                Component component = panel.getComponentAt(panelPoint);
                
                if (component instanceof JButton) {
                    currentButton = (JButton)component;
                    // Manually fire the action immediately
                    currentButton.doClick(0); // Use 0 delay
                }
            }
            // CRUCIAL: Stop editing immediately after the click event is processed.
            // This prevents the table from holding the cell in "edit mode"
            fireEditingStopped(); 
        });
        
        return panel;
    }

    // *** FIX 3: Ensure we stop editing correctly after the action fires ***
    @Override
    public boolean stopCellEditing() {
        // Return true to successfully stop editing.
        return true; 
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
    
    // ---- actions ----
    // Removed fireEditingStopped() from actions since it's now in getTableCellEditorComponent

    private void showDetails() {
        if (currentTask == null) return;
        JOptionPane.showMessageDialog(table,
                "Title: " + currentTask.getTitle()
                + "\nDescription: " + currentTask.getDescription()
                + "\nPriority: " + currentTask.getPriority()
                + "\nDeadline: " + currentTask.getDeadline()
                + "\nStatus: " + currentTask.getStatus(),
                "Task Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void markComplete() {
        if (currentTask == null) return;
        boolean ok = model.markTaskComplete(currentTask.getId());
        if (ok) {
            JOptionPane.showMessageDialog(table, "Marked complete: " + currentTask.getTitle(), "Done", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(table, "Failed to mark complete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addToToday() {
        if (currentTask == null) return;
        boolean ok = model.addToTodayQueue(currentTask);
        if (ok) {
            JOptionPane.showMessageDialog(table, "Added to today's tasks: " + currentTask.getTitle(), "Added", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(table, "Already in today's list or failed to add.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteTask() {
        if (currentTask == null) return;
        int confirm = JOptionPane.showConfirmDialog(table, "Delete task: " + currentTask.getTitle() + " ?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = model.deleteTask(currentTask.getId());
            if (ok) JOptionPane.showMessageDialog(table, "Deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            else JOptionPane.showMessageDialog(table, "Delete failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}