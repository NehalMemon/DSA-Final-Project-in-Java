package ui;

import structures.Task;
import javax.swing.*;
import java.awt.*;

public class TaskDetailWindow extends JDialog {

    public TaskDetailWindow(JFrame parent, Task task) {
        super(parent, "Task Details", true); // Modal JDialog
        setSize(400, 300);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(parent);
        
        // Use your theme colors
        Color BG_DARK = new Color(0x3d3b3c);
        Color ACCENT = new Color(0xb592a0);

        // --- Main Panel ---
        JPanel detailPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        detailPanel.setBackground(BG_DARK);
        
        // --- Labels ---
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font valueFont = new Font("Segoe UI", Font.PLAIN, 14);

        addLabel(detailPanel, "ID:", String.valueOf(task.getId()), labelFont, valueFont);
        addLabel(detailPanel, "Title:", task.getTitle(), labelFont, valueFont);
        addLabel(detailPanel, "Description:", task.getDescription(), labelFont, valueFont);
        addLabel(detailPanel, "Priority:", task.getPriority().toString(), labelFont, valueFont);
        addLabel(detailPanel, "Deadline:", task.getDeadline(), labelFont, valueFont);

        // --- OK Button ---
        JButton okButton = new JButton("OK");
        okButton.setBackground(ACCENT);
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BG_DARK);
        buttonPanel.add(okButton);

        add(detailPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    // Helper method to create and add label/value pairs
    private void addLabel(JPanel panel, String labelText, String valueText, Font labelFont, Font valueFont) {
        JLabel label = new JLabel(labelText);
        label.setFont(labelFont);
        label.setForeground(new Color(0xEEEEEE)); // Light gray for labels
        
        JLabel value = new JLabel(valueText);
        value.setFont(valueFont);
        value.setForeground(Color.WHITE);

        JPanel line = new JPanel(new BorderLayout());
        line.setBackground(null); // Keep background transparent
        line.add(label, BorderLayout.WEST);
        line.add(value, BorderLayout.CENTER);
        panel.add(line);
    }
}