package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class AddTaskForm extends JFrame {

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityBox;
    private JComboBox<String> statusBox;
    private JTextField dueDateField;
    private JTextField assigneeField;

    public AddTaskForm() {

        setTitle("Add New Task");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main Panel UI
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(12, 1, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        panel.add(new JLabel("Task Title:"));
        titleField = new JTextField();
        panel.add(titleField);

        // Description
        panel.add(new JLabel("Task Description:"));
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        panel.add(new JScrollPane(descriptionArea));

        // Priority
        panel.add(new JLabel("Priority:"));
        String[] priorities = {"Low", "Medium", "High"};
        priorityBox = new JComboBox<>(priorities);
        panel.add(priorityBox);

        // Status
        panel.add(new JLabel("Status:"));
        String[] statuses = {"To Do", "In Progress", "Done"};
        statusBox = new JComboBox<>(statuses);
        panel.add(statusBox);

        // Due Date
        panel.add(new JLabel("Due Date (yyyy-mm-dd):"));
        dueDateField = new JTextField();
        panel.add(dueDateField);

        // Assigned To
        panel.add(new JLabel("Assigned To:"));
        assigneeField = new JTextField();
        panel.add(assigneeField);

        // Add Button
        JButton addButton = new JButton("Add Task");
        panel.add(addButton);

        add(panel, BorderLayout.CENTER);

        // Button Event
                addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String title = titleField.getText().trim();
                String desc = descriptionArea.getText().trim();
                String priority = (String) priorityBox.getSelectedItem();
                String status = (String) statusBox.getSelectedItem();
                String dueDate = dueDateField.getText().trim();
                String assignee = assigneeField.getText().trim();

                // Basic validation
                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Title is required!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save to DB (Placeholder)
                saveTaskToDatabase(title, desc, priority, status, dueDate, assignee);

                JOptionPane.showMessageDialog(null,
                        "Task added successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                clearForm();
            }
        });

        setVisible(true);
    }

    // Clear UI Fields
    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        priorityBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);
        dueDateField.setText("");
        assigneeField.setText("");
    }

    // DB Placeholder Function
    private void saveTaskToDatabase(String title, String desc, String priority,
                                    String status, String dueDate, String assignee) {
        // TODO: JDBC insert code yahan likhna hai
        System.out.println("Saving task to DB:");
        System.out.println("Title: " + title);
        System.out.println("Priority: " + priority);
    }

    // Run Form for Testing
    public static void main(String[] args) {
        new AddTaskForm();
    }
}
