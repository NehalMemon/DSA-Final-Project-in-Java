package ui;

import java.awt.*;
import javax.swing.*;
import structures.Task;
import structures.TaskList;

public class AddTaskForm extends JFrame {

    private TaskList taskList;
    private TaskTableModel model;
    private int nextId = 1;

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityBox;
    private JTextField dueDateField;
    private JButton addButton;

    public AddTaskForm(TaskList taskList, TaskTableModel model) {
        this.taskList = taskList;
        this.model = model;

        setTitle("➕ Add New Task");
        setSize(540, 600);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Neon gradient background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(0, 255, 255, 50),
                        0, getHeight(), new Color(255, 0, 255, 50)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(430, 520));
        card.setBackground(new Color(25, 25, 50, 220));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 255, 255), 2, true),
                BorderFactory.createEmptyBorder(25, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        JLabel header = new JLabel("Create New Task", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(new Color(0, 255, 255));
        card.add(header, gbc);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

        gbc.gridy++;
        titleField = createTextField(fieldFont);
        addField(card, gbc, "Task Title", titleField, labelFont);

        descriptionArea = createTextArea(fieldFont);
        addArea(card, gbc, "Description", descriptionArea, labelFont);

        priorityBox = createCombo(new String[]{"low", "medium", "high"}, fieldFont);
        addField(card, gbc, "Priority", priorityBox, labelFont);

        dueDateField = createTextField(fieldFont);
        addField(card, gbc, "Due Date (DD-MM-YYYY)", dueDateField, labelFont);

        // Add Button
        addButton = new JButton("Add Task");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        addButton.setFocusPainted(false);
        addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addButton.setBackground(new Color(0, 255, 255));
        addButton.setForeground(Color.BLACK);
        addButton.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 2, true));
        addButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                addButton.setBackground(new Color(0, 255, 255, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                addButton.setBackground(new Color(0, 255, 255));
            }
        });

        gbc.gridy++;
        gbc.insets = new Insets(20, 0, 0, 0);
        card.add(addButton, gbc);

        backgroundPanel.add(card, new GridBagConstraints());
        add(backgroundPanel, BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            String desc = descriptionArea.getText().trim();
            String priorityStr = (String) priorityBox.getSelectedItem();
            String dueDate = dueDateField.getText().trim();

            if (title.isEmpty()) { showError("Title is required!"); return; }
            if (!dueDate.matches("^\\d{2}-\\d{2}-\\d{4}$")) { showError("Date must be in DD-MM-YYYY format."); return; }

            taskList.createTask(nextId++, title, desc,
                    Task.Priority.valueOf(priorityStr),
                    Task.Status.pending,
                    dueDate
            );

            model.refresh();
            showSuccess("Task added successfully!");
            clearForm();
            setVisible(false);
        });
    }

    private JTextField createTextField(Font font) {
        JTextField field = new JTextField();
        field.setFont(font);
        field.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 2, true));
        field.setBackground(new Color(15, 15, 25));
        field.setForeground(Color.WHITE);
        return field;
    }

    private JTextArea createTextArea(Font font) {
        JTextArea area = new JTextArea(4, 20);
        area.setFont(font);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 2, true));
        area.setBackground(new Color(15, 15, 25));
        area.setForeground(Color.WHITE);
        return area;
    }

    private JComboBox<String> createCombo(String[] list, Font font) {
        JComboBox<String> box = new JComboBox<>(list);
        box.setFont(font);
        box.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 2, true));
        box.setBackground(new Color(15, 15, 25));
        box.setForeground(Color.WHITE);
        return box;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent comp, Font font) {
        gbc.gridy++;
        JLabel lbl = new JLabel(label);
        lbl.setFont(font);
        lbl.setForeground(new Color(0, 255, 255));
        panel.add(lbl, gbc);

        gbc.gridy++;
        panel.add(comp, gbc);
    }

    private void addArea(JPanel panel, GridBagConstraints gbc, String label, JTextArea area, Font font) {
        gbc.gridy++;
        JLabel lbl = new JLabel(label);
        lbl.setFont(font);
        lbl.setForeground(new Color(0, 255, 255));
        panel.add(lbl, gbc);

        gbc.gridy++;
        JScrollPane pane = new JScrollPane(area);
        pane.setBorder(null);
        panel.add(pane, gbc);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        priorityBox.setSelectedIndex(0);
        dueDateField.setText("");
    }
}
