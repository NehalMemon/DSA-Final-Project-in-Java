package ui;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import structures.TaskList;
import structures.Task;
import structures.TaskQueue;
import structures.TaskStack;
import structures.SortingAndSearch;

public class MainWindow extends JFrame {

    // Theme Colors
    private final Color BG_DARK = new Color(0x3d3b3c);
    private final Color ACCENT = new Color(0xb592a0);
    private final Color ACCENT2 = new Color(0x7a9e9f);
    private final Color SUCCESS = new Color(0x6bffb8);
    private final Color WARNING = new Color(0xbc5f04);

    private TaskList taskList;
    private TaskQueue todayQueue;
    private TaskStack completedStack;

    private TaskTableModel model;
    private JTable taskTable;

    private JTextField searchField;

    public MainWindow() {

        this.taskList = new SortingAndSearch();
        this.todayQueue = new TaskQueue();
        this.completedStack = new TaskStack();

        // Sample Data
        taskList.createTask(1, "Refactor DB", "Update JDBC", Task.Priority.high, Task.Status.pending, "14-11-25");
        taskList.createTask(2, "Review PRs", "Code review", Task.Priority.medium, Task.Status.pending, "12-12-25");
        taskList.createTask(3, "Write docs", "API docs", Task.Priority.medium, Task.Status.pending, "20-11-25");
        taskList.createTask(4, "Hotfix", "Payment bug", Task.Priority.high, Task.Status.pending, "02-12-25");
        taskList.createTask(5, "Brainstorm", "Q1 prep", Task.Priority.low, Task.Status.pending, "03-12-25");

        setTitle("Smart Study Manager");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // GLOBAL FONT
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 12));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 14));

        // ================= TOP BAR (ALL COMPONENTS ON ONE LINE) =================
        JPanel topBar = new JPanel();
        topBar.setBackground(BG_DARK);
        topBar.setLayout(new BoxLayout(topBar, BoxLayout.X_AXIS));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        // LEFT BUTTONS
        JButton addTaskBtn = styledButton("Add", ACCENT);
        JButton viewCompleted = styledButton("Completed", ACCENT2);
        JButton viewToday = styledButton("Today", SUCCESS);

        // SEARCH FIELD + BUTTON
        searchField = new JTextField(25);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton searchBtn = styledButton("Search", ACCENT2);

        // RIGHT BUTTONS
        JButton sortPriorityBtn = styledButton("Sort: Priority", WARNING);
        JButton sortDeadlineBtn = styledButton("Sort: Deadline", WARNING);
        JButton refreshBtn = styledButton("Refresh", ACCENT2.darker());

        // Add all components to topBar with horizontal spacing
        topBar.add(addTaskBtn);
        topBar.add(Box.createRigidArea(new Dimension(10, 0)));
        topBar.add(viewCompleted);
        topBar.add(Box.createRigidArea(new Dimension(10, 0)));
        topBar.add(viewToday);
        topBar.add(Box.createRigidArea(new Dimension(20, 0))); // bigger gap before search
        topBar.add(searchField);
        topBar.add(Box.createRigidArea(new Dimension(5, 0)));
        topBar.add(searchBtn);
        topBar.add(Box.createHorizontalGlue()); // pushes remaining buttons to right
        topBar.add(sortPriorityBtn);
        topBar.add(Box.createRigidArea(new Dimension(10, 0)));
        topBar.add(sortDeadlineBtn);
        topBar.add(Box.createRigidArea(new Dimension(10, 0)));
        topBar.add(refreshBtn);

        add(topBar, BorderLayout.NORTH);

        // ================= TABLE SECTION =================
        model = new TaskTableModel(taskList, todayQueue, completedStack);
        taskTable = new JTable(model);
        taskTable.setRowHeight(40);
        taskTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        taskTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        taskTable.getTableHeader().setBackground(ACCENT);
        taskTable.getTableHeader().setForeground(Color.WHITE);

        TaskRowRenderer rowRenderer = new TaskRowRenderer(model);
        taskTable.setDefaultRenderer(Object.class, rowRenderer);

        TaskActionRenderer actionRenderer = new TaskActionRenderer();
        TableColumn actionColumn = taskTable.getColumnModel().getColumn(3);
        actionColumn.setCellRenderer(actionRenderer);
        actionColumn.setCellEditor(new TaskActionEditor(model, taskTable));

        JScrollPane scroll = new JScrollPane(taskTable);
        scroll.getViewport().setBackground(new Color(0x2f2f2f));
        add(scroll, BorderLayout.CENTER);

        // ================= ACTIONS =================
        sortPriorityBtn.addActionListener(e -> model.sortByPriority());
        sortDeadlineBtn.addActionListener(e -> model.sortByDeadline());
        refreshBtn.addActionListener(e -> model.refresh());

        searchBtn.addActionListener(e -> {
            // 1. Get the search term from the text field
            String searchTerm = searchField.getText().trim();
        
            if (searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a task title to search.", "Input Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
        
            Task foundTask = taskList.searchByTitle(searchTerm);
            if (foundTask != null) {
                TaskDetailWindow detailWindow = new TaskDetailWindow(this, foundTask);
                detailWindow.setVisible(true);
            } else {
                // Task not found: Show a message
                JOptionPane.showMessageDialog(this,
                        "Task with title '" + searchTerm + "' was not found.",
                        "Search Failed",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        viewToday.addActionListener(e -> {
            TodayTasksWindow tw = new TodayTasksWindow(todayQueue, completedStack, taskList);
            tw.setVisible(true);
        });

        viewCompleted.addActionListener(e -> {
            CompletedTasks cw = new CompletedTasks(taskList, completedStack);
            cw.setVisible(true);
        });

        addTaskBtn.addActionListener(e -> {
            AddTaskForm form = new AddTaskForm(taskList, model);
            form.setVisible(true);
        });

        model.refresh();
    }

    // ==================== ROUNDED PILL BUTTON ====================
    private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                super.paintComponent(g2);
                g2.dispose();
            }

            @Override
            public void paintBorder(Graphics g) {
                // no border
            }
        };
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
}
}
