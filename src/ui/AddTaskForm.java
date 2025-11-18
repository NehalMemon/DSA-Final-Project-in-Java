package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import structures.Task;
import structures.TaskList;

public class AddTaskForm extends JFrame {

    // Theme Colors
    private final Color BG_DARK = new Color(0x3d3b3c);
    private final Color ACCENT = new Color(0xb592a0);
    private final Color ACCENT2 = new Color(0x7a9e9f);
    private final Color SUCCESS = new Color(0x6bffb8);
    private final Color WARNING = new Color(0xbc5f04);

    // Derived Colors
    private final Color CARD_BG = new Color(0x2f2f2f); // Lighter dark for the card panel
    private final Color FIELD_BG = new Color(0x454545); // Background for input fields
    private final Color FIELD_BORDER = ACCENT2.darker(); // Border for input fields
    private final Color TEXT_LIGHT = Color.WHITE;
    private final Color LABEL_FOREGROUND = ACCENT; // Using ACCENT for labels

    private TaskList taskList;
    private TaskTableModel model; 
    private int nextId = 1; // Default starting ID

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityBox;
    private JTextField dueDateField; // Keeping as JTextField, enforcing DD-MM-YYYY
    private JButton addButton;

    // Pattern for strict DD-MM-YYYY format validation
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}-\\d{2}-\\d{4}$");

    public AddTaskForm(TaskList taskList, TaskTableModel model) { 
        this.taskList = taskList;
        this.model = model;
        
        // Find the highest ID and start generating from there to prevent collisions
        Task lastTask = taskList.get(taskList.size() - 1);
        if (lastTask != null) {
            this.nextId = lastTask.getId() + 1;
        }

        setTitle("Add New Task");
        setSize(480, 500); // Reduced height since fields are removed
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        // Main Background
        getContentPane().setBackground(BG_DARK); // Applied BG_DARK

        // Card Panel (Modern Look)
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(CARD_BG); // Applied CARD_BG
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        // --- THEMED FONT SETUP ---
        Font font = new Font("Segoe UI", Font.PLAIN, 14); // Changed to Segoe UI for consistency
        UIManager.put("Label.foreground", LABEL_FOREGROUND); // Applied ACCENT for UIManager default

        // TextField Style
        titleField = makeTextField(font);
        descriptionArea = makeTextArea(font);
        dueDateField = makeTextField(font);

        // Priority enums are low, medium, high (match Task.Priority)
        priorityBox = makeCombo(new String[]{"low", "medium", "high"}, font); 

        // --- Add Components ---
        addField(panel, gbc, "Task Title:", titleField, font);
        addArea(panel, gbc, "Task Description:", descriptionArea, font);
        addField(panel, gbc, "Priority:", priorityBox, font);
        // UPDATED DATE PROMPT
        addField(panel, gbc, "Due Date (DD-MM-YYYY):", dueDateField, font); 
        
        // Modern Button
        addButton = new JButton("Add Task");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14)); // Consistent bold font
        addButton.setBackground(ACCENT2); // Applied ACCENT2 for the main button
        addButton.setForeground(TEXT_LIGHT); // Applied TEXT_LIGHT
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy++;
        panel.add(addButton, gbc);

        add(panel);

        // Button Action
        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String desc = descriptionArea.getText().trim();
            String priorityStr = (String) priorityBox.getSelectedItem();
            String dueDate = dueDateField.getText().trim();

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Title is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // DATE VALIDATION
            if (!DATE_PATTERN.matcher(dueDate).matches()) {
                JOptionPane.showMessageDialog(null, "Due Date must be in DD-MM-YYYY format!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Status is implicitly 'pending' since we removed the field
            saveTask(title, desc, priorityStr, Task.Status.pending.name(), dueDate);

            JOptionPane.showMessageDialog(null, "Task added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            clearForm();
            setVisible(false);
        });
    }

    // --------------------------- UI HELPERS -----------------------------

    private JTextField makeTextField(Font f) {
        JTextField field = new JTextField();
        field.setFont(f);
        field.setBackground(FIELD_BG); // Applied FIELD_BG
        field.setForeground(TEXT_LIGHT); // Applied TEXT_LIGHT
        field.setCaretColor(TEXT_LIGHT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(FIELD_BORDER, 1), // Applied FIELD_BORDER
            BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        return field;
    }

    private JTextArea makeTextArea(Font f) {
        JTextArea area = new JTextArea(3, 20);
        area.setFont(f);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(FIELD_BG); // Applied FIELD_BG
        area.setForeground(TEXT_LIGHT); // Applied TEXT_LIGHT
        area.setCaretColor(TEXT_LIGHT);
        area.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(FIELD_BORDER, 1), // Applied FIELD_BORDER
            BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        return area;
    }

    private JComboBox<String> makeCombo(String[] list, Font f) {
        JComboBox<String> box = new JComboBox<>(list);
        box.setFont(f);
        box.setBackground(FIELD_BG); // Applied FIELD_BG
        box.setForeground(TEXT_LIGHT); // Applied TEXT_LIGHT
        return box;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String text, JComponent comp, Font f) {
        JLabel label = new JLabel(text);
        label.setFont(f.deriveFont(Font.BOLD)); // Labels should be bold
        label.setForeground(LABEL_FOREGROUND); // Set label foreground here to override UIManager for safety
        gbc.gridy++;
        panel.add(label, gbc);
        gbc.gridy++;
        panel.add(comp, gbc);
    }

    private void addArea(JPanel panel, GridBagConstraints gbc, String text, JTextArea area, Font f) {
        JLabel label = new JLabel(text);
        label.setFont(f.deriveFont(Font.BOLD)); // Labels should be bold
        label.setForeground(LABEL_FOREGROUND); // Set label foreground here
        gbc.gridy++;
        panel.add(label, gbc);
        gbc.gridy++;
        // Use the themed colors for the scroll pane viewport as well
        JScrollPane scroll = new JScrollPane(area);
        scroll.getViewport().setBackground(FIELD_BG);
        scroll.setBorder(BorderFactory.createLineBorder(FIELD_BORDER, 1));
        panel.add(scroll, gbc);
    }

    // ----------------------- FUNCTIONALITY ------------------------

    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        priorityBox.setSelectedIndex(0);
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