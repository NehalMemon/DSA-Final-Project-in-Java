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

public class TaskActionEditor extends DefaultCellEditor {

    private JPanel panel;
    private JButton detailButton, completeButton, todayButton, deleteButton;
    private TaskTableModel model;
    private JTable table;
    private Task currentTask;

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
            // FIX: Call markTaskComplete() directly on the model
            if (model.markTaskComplete(currentTask.getId())) { 
                JOptionPane.showMessageDialog(table, "Task " + currentTask.getTitle() + " marked complete!", "Action", JOptionPane.INFORMATION_MESSAGE);
            }
            fireEditingStopped();
        });
        
        // FIX: Added initialization for todayButton to resolve NullPointerException
        todayButton = createButton("⭐", "Add to Today", e -> {
            JOptionPane.showMessageDialog(table, "Task " + currentTask.getTitle() + " added to today's list.", "Action", JOptionPane.INFORMATION_MESSAGE);
            fireEditingStopped();
        });
        
        // NEW BUTTON: Delete Logic
        deleteButton = createButton("🗑️", "Delete Task", e -> {
            // Declare 'confirm' inside the listener scope
            int confirm = JOptionPane.showConfirmDialog(table, 
                "Are you sure you want to delete task: " + currentTask.getTitle() + "?", 
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                // FIX: Call deleteTask() directly on the model
                if (model.deleteTask(currentTask.getId())) { 
                    JOptionPane.showMessageDialog(table, "Task " + currentTask.getTitle() + " deleted.", "Action", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(table, "Failed to delete task.", "Action Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            fireEditingStopped();
        });

        // The buttons are now guaranteed to be non-null when added
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
        return button;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentTask = model.getTaskAt(row);
        
        // Use the renderer to get the row color and apply it to the editor panel
        Color rowColor = table.getDefaultRenderer(Object.class).getTableCellRendererComponent(table, value, isSelected, false, row, column).getBackground();
        panel.setBackground(rowColor);
        
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        // We don't need to return a value, as the action is handled in the listener
        return ""; 
    }
}