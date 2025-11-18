package ui;

import javax.swing.*;
import java.awt.*;
import structures.Task;
import structures.TaskList;
import structures.TaskStack;
import structures.TaskQueue;

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

        // Styling
        getContentPane().setBackground(new Color(20, 20, 20));
        Font headerFont = new Font("Montserrat", Font.BOLD, 20);
        Font buttonFont = new Font("Montserrat", Font.PLAIN, 14);

        // --- HEADER PANEL ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(32, 32, 32));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("📋 My Task Manager");
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(new Color(0, 122, 255));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Button Panel (Top Right)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(32, 32, 32));

        JButton addTaskButton = createStyledButton("+ Add Task", buttonFont);
        JButton viewCompletedButton = createStyledButton("✓ View Completed", buttonFont);
        JButton viewTodayButton = createStyledButton("⭐ Today's Tasks", buttonFont);

        buttonPanel.add(addTaskButton);
        buttonPanel.add(viewCompletedButton);
        buttonPanel.add(viewTodayButton);

        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // --- TABLE SETUP ---
        tableModel = new TaskTableModel(taskList);
        taskTable = new JTable(tableModel);

        // Table Styling
        taskTable.setFont(new Font("Montserrat", Font.PLAIN, 14));
        taskTable.setRowHeight(40);
        taskTable.setGridColor(new Color(50, 50, 50));
        taskTable.setShowGrid(true);
        taskTable.setSelectionBackground(new Color(50, 50, 50));
        taskTable.setSelectionForeground(Color.WHITE);

        // Table Header
        taskTable.getTableHeader().setBackground(new Color(45, 45, 45));
        taskTable.getTableHeader().setForeground(Color.WHITE);
        taskTable.getTableHeader().setFont(new Font("Montserrat", Font.BOLD, 14));

        // Column widths
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(300); // Title
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Priority
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Deadline
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Actions

        // Set custom renderers and editors
        TaskRowRenderer rowRenderer = new TaskRowRenderer(tableModel);
        taskTable.setDefaultRenderer(Object.class, rowRenderer);

        TaskActionRenderer actionRenderer = new TaskActionRenderer();
        taskTable.getColumn("Actions").setCellRenderer(actionRenderer);

        TaskActionEditor actionEditor = new TaskActionEditor(tableModel, taskTable);
        taskTable.getColumn("Actions").setCellEditor(actionEditor);

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.getViewport().setBackground(new Color(32, 32, 32));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
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
            completedTasksWindow.refreshTable(); // Refresh before showing
            completedTasksWindow.setVisible(true);
            // Refresh main window when completed tasks window closes/updates
            tableModel.refresh();
        });

        viewTodayButton.addActionListener(e -> {
            showTodaysTasks();
        });

        // Add some sample data for testing
        addSampleData();
    }

    private JButton createStyledButton(String text, Font font) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(new Color(0, 122, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
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
        // Create a simple dialog to show today's tasks
        JDialog dialog = new JDialog(this, "Today's Tasks", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(32, 32, 32));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel("Tasks in Today's Queue:");
        label.setFont(new Font("Montserrat", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        panel.add(label, BorderLayout.NORTH);

        // Display queue contents
        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Montserrat", Font.PLAIN, 14));
        textArea.setBackground(new Color(45, 45, 45));
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
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}