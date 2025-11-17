package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import structures.Task;
import structures.TaskList;

public class AddTaskForm extends JFrame {

    private TaskList taskList;
    private TaskTableModel model; 
    private int nextId = 5; // Start ID after the sample data in MainApp (adjust if needed)

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityBox;
    private JComboBox<String> statusBox;
    private JTextField dueDateField;
    private JTextField assigneeField;
    private JButton addButton; // FIX: Declared addButton here

    // Updated Constructor to accept TaskList and TaskTableModel
    public AddTaskForm(TaskList taskList, TaskTableModel model) { 
        this.taskList = taskList;
        this.model = model;
        
        // Find the highest ID and start generating from there to prevent collisions
        Task lastTask = taskList.get(taskList.size() - 1);
        if (lastTask != null) {
            this.nextId = lastTask.getId() + 1;
        }

        setTitle("Add New Task");
        setSize(480, 620);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        // Main Background
        getContentPane().setBackground(new Color(20, 20, 20));

        // Card Panel (Modern Look)
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(32, 32, 32));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        // --- MODERN FONT ---
        Font font = new Font("Montserrat", Font.PLAIN, 14);

        // Label Style
        UIManager.put("Label.foreground", new Color(230, 230, 230));

        // TextField Style
        titleField = makeTextField(font);
        descriptionArea = makeTextArea(font);
        dueDateField = makeTextField(font);
        assigneeField = makeTextField(font);

        // Priority enums are low, medium, high (must match Task.Priority)
        priorityBox = makeCombo(new String[]{"low", "medium", "high"}, font); 
        // Status enums are pending, completed (match Task.Status)
        statusBox = makeCombo(new String[]{"pending", "completed"}, font);

        // --- Add Components ---
        addField(panel, gbc, "Task Title:", titleField, font);
        addArea(panel, gbc, "Task Description:", descriptionArea, font);
        addField(panel, gbc, "Priority:", priorityBox, font);
        addField(panel, gbc, "Status:", statusBox, font);
        addField(panel, gbc, "Due Date (yyyy-mm-dd):", dueDateField, font);
        addField(panel, gbc, "Assigned To:", assigneeField, font);

        // Modern Button (Now using the declared field)
        addButton = new JButton("Add Task"); // FIX: Initialization of addButton
        addButton.setFont(font);
        addButton.setBackground(new Color(0, 122, 255));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy++;
        panel.add(addButton, gbc);

        add(panel);

        // Button Action
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String title = titleField.getText().trim();
                String desc = descriptionArea.getText().trim();
                String priorityStr = (String) priorityBox.getSelectedItem();
                String statusStr = (String) statusBox.getSelectedItem();
                String dueDate = dueDateField.getText().trim();
                String assignee = assigneeField.getText().trim();

                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Title is required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                saveTask(title, desc, priorityStr, statusStr, dueDate, assignee);

                JOptionPane.showMessageDialog(null, "Task added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearForm(); // FIX: Now the method is defined below
                setVisible(false);
            }
        });
    }

    // --------------------------- UI HELPERS -----------------------------

    private JTextField makeTextField(Font f) {
        JTextField field = new JTextField();
        field.setFont(f);
        field.setBackground(new Color(45, 45, 45));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        return field;
    }

    private JTextArea makeTextArea(Font f) {
        JTextArea area = new JTextArea(3, 20);
        area.setFont(f);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(new Color(45, 45, 45));
        area.setForeground(Color.WHITE);
        area.setCaretColor(Color.WHITE);
        area.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        return area;
    }

    private JComboBox<String> makeCombo(String[] list, Font f) {
        JComboBox<String> box = new JComboBox<>(list);
        box.setFont(f);
        box.setBackground(new Color(45, 45, 45));
        box.setForeground(Color.WHITE);
        return box;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String text, JComponent comp, Font f) {
        JLabel label = new JLabel(text);
        label.setFont(f);
        gbc.gridy++;
        panel.add(label, gbc);
        gbc.gridy++;
        panel.add(comp, gbc);
    }

    private void addArea(JPanel panel, GridBagConstraints gbc, String text, JTextArea area, Font f) {
        JLabel label = new JLabel(text);
        label.setFont(f);
        gbc.gridy++;
        panel.add(label, gbc);
        gbc.gridy++;
        panel.add(new JScrollPane(area), gbc);
    }

    // ----------------------- FUNCTIONALITY ------------------------

    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        priorityBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);
        dueDateField.setText("");
    }

    private void saveTask(String title, String desc, String priorityStr,
                          String statusStr, String dueDate) {
        
        // Convert string values to Task Enums
        Task.Priority priority = Task.Priority.valueOf(priorityStr);
        Task.Status status = Task.Status.valueOf(statusStr); 

        // Create the task and add it to the list
        taskList.createTask(nextId++, title, desc, priority, status, dueDate);
        
        // Notify the main table to redraw with the new task
        model.refresh(); 
    }
}