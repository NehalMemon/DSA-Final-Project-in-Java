package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTaskForm extends JFrame {

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityBox;
    private JComboBox<String> statusBox;
    private JTextField dueDateField;
    private JTextField assigneeField;

    public AddTaskForm() {

        setTitle("Add New Task");
        setSize(480, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        // Main Background
        getContentPane().setBackground(new Color(20, 20, 20));  // Dark BG

        // Card Panel (Modern Look)
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(32, 32, 32)); // Card color
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

        priorityBox = makeCombo(new String[]{"Low", "Medium", "High"}, font);
        statusBox = makeCombo(new String[]{"To Do", "In Progress", "Done"}, font);

        // --- Add Components ---

        addField(panel, gbc, "Task Title:", titleField, font);
        addArea(panel, gbc, "Task Description:", descriptionArea, font);
        addField(panel, gbc, "Priority:", priorityBox, font);
        addField(panel, gbc, "Status:", statusBox, font);
        addField(panel, gbc, "Due Date (yyyy-mm-dd):", dueDateField, font);
        addField(panel, gbc, "Assigned To:", assigneeField, font);

        // Modern Button
        JButton addButton = new JButton("Add Task");
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
                String priority = (String) priorityBox.getSelectedItem();
                String status = (String) statusBox.getSelectedItem();
                String dueDate = dueDateField.getText().trim();
                String assignee = assigneeField.getText().trim();

                if (title.isEmpty()) {
                    JOptionPane.showMessageDialog(null,
                            "Title is required!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                saveTaskToDatabase(title, desc, priority, status, dueDate, assignee);

                JOptionPane.showMessageDialog(null,
                        "Task added successfully!",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                clearForm();
            }
        });

        setVisible(true);
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
        assigneeField.setText("");
    }

    private void saveTaskToDatabase(String title, String desc, String priority,
                                    String status, String dueDate, String assignee) {
        System.out.println("Saving task to DB: " + title);
    }
}
