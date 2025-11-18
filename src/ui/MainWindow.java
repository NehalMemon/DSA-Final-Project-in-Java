package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import structures.Task;
import structures.TaskList;
import structures.TaskQueue;
import structures.TaskStack;

public class MainWindow extends JFrame {

    private TaskList taskList;
    private TaskStack completedStack;
    private TaskQueue todayQueue;

    private TaskTableModel tableModel;
    private JTable taskTable;

    private AddTaskForm addTaskForm;
    private CompletedTasks completedTasksWindow;
    private TodayTasksWindow todayTasksWindow;

    public MainWindow() {
        // Initialize data
        taskList = new TaskList();
        completedStack = new TaskStack();
        todayQueue = new TaskQueue();

        // Window setup
        setTitle("Task Manager - To-Do List");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(42, 42, 42)); // dark background

        // Fonts
        Font headerFont = new Font("Segoe UI", Font.BOLD, 22);
        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);

        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 30, 35));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📋 My Task Manager");
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(new Color(0, 255, 255));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(30, 30, 35));

        JButton addTaskButton = createNeonButton("+ Add Task", buttonFont);
        JButton viewCompletedButton = createNeonButton("✓ View Completed", buttonFont);
        JButton viewTodayButton = createNeonButton("⭐ Today's Tasks", buttonFont);

        buttonPanel.add(addTaskButton);
        buttonPanel.add(viewCompletedButton);
        buttonPanel.add(viewTodayButton);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- Table ---
        tableModel = new TaskTableModel(taskList);
        taskTable = new JTable(tableModel);
        taskTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        taskTable.setRowHeight(45);
        taskTable.setShowGrid(true);
        taskTable.setGridColor(new Color(60, 60, 60));
        taskTable.setSelectionBackground(new Color(0, 255, 255, 60));
        taskTable.setSelectionForeground(Color.BLACK);
        taskTable.setBackground(new Color(42, 42, 42));
        taskTable.setForeground(Color.WHITE);
        taskTable.setAutoCreateRowSorter(true);

        // Table Header
        taskTable.getTableHeader().setBackground(new Color(35, 35, 40));
        taskTable.getTableHeader().setForeground(new Color(0, 255, 255));
        taskTable.getTableHeader().setFont(headerFont.deriveFont(Font.BOLD, 15));
        taskTable.getTableHeader().setBorder(new LineBorder(new Color(0, 255, 255)));

        // Column widths
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(300); // Title
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Priority
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Deadline
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Actions

        // Renderer for neon rows
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(new Color(0, 255, 255, 50));
                    c.setForeground(Color.BLACK);
                } else if (row % 2 == 0) {
                    c.setBackground(new Color(45, 45, 50));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(new Color(42, 42, 45));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            taskTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        scrollPane.getViewport().setBackground(new Color(42, 42, 42));
        add(scrollPane, BorderLayout.CENTER);

        // --- Button Actions ---
        addTaskButton.addActionListener(e -> {
            if (addTaskForm == null) addTaskForm = new AddTaskForm(taskList, tableModel);
            addTaskForm.setVisible(true);
        });

        viewCompletedButton.addActionListener(e -> {
            if (completedTasksWindow == null)
                completedTasksWindow = new CompletedTasks(taskList, completedStack);
            completedTasksWindow.refreshTable();
            completedTasksWindow.setVisible(true);
            tableModel.refresh();
        });

        viewTodayButton.addActionListener(e -> showTodaysTasks());

        // Sample data
        addSampleData();
    }

    private JButton createNeonButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(new Color(0, 255, 255));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 200, 200), 2, true),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(0, 200, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(new Color(0, 255, 255));
            }
        });
        return button;
    }

    private void addSampleData() {
        taskList.createTask(1, "Complete Java Project", "Finish the to-do list application",
                Task.Priority.high, Task.Status.pending, "25-11-2024");
        taskList.createTask(2, "Buy Groceries", "Get milk, eggs, and bread",
                Task.Priority.medium, Task.Status.pending, "20-11-2024");
        taskList.createTask(3, "Read Book", "Finish reading 'Clean Code'",
                Task.Priority.low, Task.Status.pending, "30-11-2024");
        tableModel.refresh();
    }

    private void showTodaysTasks() {
        JDialog dialog = new JDialog(this, "Today's Tasks", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(42, 42, 42));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Tasks in Today's Queue:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(0, 255, 255));
        panel.add(label, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setBackground(new Color(45, 45, 50));
        textArea.setForeground(Color.WHITE);
        textArea.setEditable(false);

        Task[] todayTasks = todayQueue.getAllTasks();
        if (todayTasks.length == 0) {
            textArea.setText("No tasks in today's queue.\n\nUse the '⭐ Add to Today' button to add tasks.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < todayTasks.length; i++) {
                Task task = todayTasks[i];
                sb.append((i + 1)).append(". ").append(task.getTitle())
                        .append(" (").append(task.getPriority()).append(")\n");
            }
            textArea.setText(sb.toString());
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
