package ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import structures.Task;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font; // Import Font

public class TaskActionEditor extends DefaultCellEditor {

    private JPanel panel;
    private JButton detailButton, completeButton, todayButton, deleteButton;
    private TaskTableModel model;
    private JTable table;
    private Task currentTask;

    // Define a font that supports emojis (e.g., Arial Unicode MS or similar system default)
    // We'll use a larger size so the emojis are more visible
    private static final Font EMOJI_FONT = new Font("Segoe UI Symbol", Font.PLAIN, 16);
    // Fallback if "Segoe UI Symbol" isn't available
    // On some systems, just "Arial" at a larger size works, but let's try a specific one first.

    public TaskActionEditor(TaskTableModel model, JTable table) {
        super(new JTextField());
        this.model = model;
        this.table = table;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setOpaque(true);

        detailButton = createButton("🔍", "View Details", e -> {
            JOptionPane.showMessageDialog(table, 
                "Viewing Task:\nTitle: " + currentTask.getTitle() + 
                "\nPriority: " + currentTask.getPriority().name() + 
                "\nDeadline: " + currentTask.getDeadline(), "Task Details", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        });

        completeButton = createButton("✓", "Mark Complete", e -> {
            if (model.markTaskComplete(currentTask.getId())) { 
                JOptionPane.showMessageDialog(table, "Task " + currentTask.getTitle() + " marked complete and added to stack!", "Action", JOptionPane.INFORMATION_MESSAGE);
            }
            fireEditingStopped();
        });
        
        todayButton = createButton("⭐", "Add to Today", e -> {
            // // FIX: You need to implement the logic to add the task to the TaskQueue here
            // if (model.addToTodayQueue(currentTask)) {
            //      JOptionPane.showMessageDialog(table, "Task " + currentTask.getTitle() + " added to today's list.", "Action", JOptionPane.INFORMATION_MESSAGE);
            // } else {
            //      JOptionPane.showMessageDialog(table, "Task " + currentTask.getTitle() + " is already in today's list.", "Action", JOptionPane.WARNING_MESSAGE);
            // }
            fireEditingStopped();
        });
        
        deleteButton = createButton("🗑️", "Delete Task", e -> {
            int confirm = JOptionPane.showConfirmDialog(table, 
                "Are you sure you want to delete task: " + currentTask.getTitle() + "?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (model.deleteTask(currentTask.getId())) { 
                    JOptionPane.showMessageDialog(table, "Task " + currentTask.getTitle() + " deleted.", "Action", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(table, "Failed to delete task.", "Action Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            fireEditingStopped();
        });

        panel.add(detailButton);
        panel.add(completeButton);
        panel.add(todayButton);
        panel.add(deleteButton); 
    }

    private JButton createButton(String text, String tooltip, ActionListener listener) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(30, 25));
        button.setFocusable(false);
        button.addActionListener(listener);
        
        // --- FIX: Set the emoji-supporting font here ---
        button.setFont(EMOJI_FONT);
        // The buttons are small, we can set the text to be empty to show just the icon/emoji
        // If the emoji is still not visible, you might need to try a different font name 
        // that is common on your specific operating system (e.g., "Arial Unicode MS").
        
        return button;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // Need to convert the view row index to the model row index if the table is sorted
        int modelRow = table.convertRowIndexToModel(row);
        currentTask = model.getTaskAt(modelRow);
        
        // Use the renderer to get the row color and apply it to the editor panel
        Color rowColor = table.getDefaultRenderer(Object.class).getTableCellRendererComponent(table, value, isSelected, false, row, column).getBackground();
        panel.setBackground(rowColor);
        
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return ""; 
    }
}