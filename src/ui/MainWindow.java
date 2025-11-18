package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
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

    public MainWindow() {
        // Initialize data structures
        taskList = new TaskList();
        completedStack = new TaskStack();
        todayQueue = new TaskQueue();

        // Setup main window
        setTitle("Task Manager - To-Do List");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Neon theme colors
        Color neonBlue = new Color(0, 255, 255);
        Color neonPink = new Color(255, 0, 255);
        Color darkBg = new Color(15, 15, 25);

        // --- HEADER PANEL ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(darkBg);
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📋 My Task Manager");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(neonBlue);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Top-right buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(darkBg);

        JButton addTaskButton = createStyledButton("+ Add Task", neonBlue);
        JButton viewCompletedButton = createStyledButton("✓ View Completed", neonPink);
        JButton viewTodayButton = createStyledButton("⭐ Today's Tasks", neonBlue);

        buttonPanel.add(addTaskButton);
        buttonPanel.add(viewCompletedButton);
        buttonPanel.add(viewTodayButton);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- TABLE SETUP ---
        tableModel = new TaskTableModel(taskList);
        taskTable = new JTable(tableModel);

        // Table styling
        taskTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        taskTable.setRowHeight(42);
        taskTable.setGridColor(new Color(50, 50, 50));
        taskTable.setShowGrid(true);
        taskTable.setBackground(darkBg);
        taskTable.setForeground(Color.WHITE);
        taskTable.setSelectionBackground(new Color(0, 255, 255, 80));
        taskTable.setSelectionForeground(Color.BLACK);

        // Table header
        JTableHeader header = taskTable.getTableHeader();
        header.setBackground(new Color(25, 25, 40));
        header.setForeground(neonBlue);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setReorderingAllowed(false);

        // Column widths
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(300);
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(200);

        // Custom row renderer
        taskTable.setDefaultRenderer(Object.class, new TaskRowRenderer(tableModel, new Color(0, 255, 255, 40)));

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setViewportBorder(null);
        scrollPane.getViewport().setBackground(darkBg);
        add(scrollPane, BorderLayout.CENTER);

        // --- BUTTON ACTIONS ---
        addTaskButton.addActionListener(e -> {
            if (addTaskForm == null) {
                addTaskForm = new AddTaskForm(taskList, tableModel);
            }
            addTaskForm.setVisible(true);
        });

        viewCompletedButton.addActionListener(e -> {
            if (completedTasksWindow == null) {
                completedTasksWindow = new CompletedTasks(taskList, completedStack);
            }
            completedTasksWindow.refreshTable();
            completedTasksWindow.setVisible(true);
            tableModel.refresh();
        });

        viewTodayButton.addActionListener(e -> showTodaysTasks());

        // Sample data
        addSampleData();
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        panel.setBackground(new Color(20, 20, 30));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Tasks in Today's Queue:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(new Color(0, 255, 255));
        panel.add(label, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setBackground(new Color(25, 25, 35));
        textArea.setForeground(Color.WHITE);
        textArea.setEditable(false);

        Task[] todayTasks = todayQueue.getAllTasks();
        if (todayTasks.length == 0) {
            textArea.setText("No tasks in today's queue.\nUse the '⭐ Add to Today' button to add tasks.");
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
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }

    // --- Custom Row Renderer for neon effect ---
    class TaskRowRenderer extends DefaultTableCellRenderer {
        private TaskTableModel model;
        private Color highlightColor;

        public TaskRowRenderer(TaskTableModel model, Color highlightColor) {
            this.model = model;
            this.highlightColor = highlightColor;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (isSelected) {
                c.setBackground(new Color(0, 255, 255, 80));
            } else if (row % 2 == 0) {
                c.setBackground(highlightColor);
            } else {
                c.setBackground(new Color(15, 15, 25));
            }
            c.setForeground(Color.WHITE);
            return c;
        }
    }
}
