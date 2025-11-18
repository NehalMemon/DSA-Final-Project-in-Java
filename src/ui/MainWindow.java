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

        setTitle("Task Manager");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // GLOBAL FONT
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 14));

        // Top Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(BG_DARK);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        // Button Style
        UIManager.put("Button.focus", new Color(0,0,0,0));

        // LEFT BUTTONS
        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftButtons.setBackground(BG_DARK);

        JButton addTaskBtn = styledButton("➕ Add", ACCENT);
        JButton viewCompleted = styledButton("✔ Completed", ACCENT2);
        JButton viewToday = styledButton("📋 Today", SUCCESS);

        leftButtons.add(addTaskBtn);
        leftButtons.add(viewCompleted);
        leftButtons.add(viewToday);

        // CENTER SEARCH BAR
        JPanel centerSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        centerSearch.setBackground(BG_DARK);

        searchField = new JTextField(28);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton searchBtn = styledButton("🔎", ACCENT2);

        centerSearch.add(searchField);
        centerSearch.add(searchBtn);

        // RIGHT BUTTONS (SORT + REFRESH)
        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightButtons.setBackground(BG_DARK);

        JButton sortPriorityBtn = styledButton("Sort: Priority", WARNING);
        JButton sortDeadlineBtn = styledButton("Sort: Deadline", WARNING);
        
        // *** NEW: Refresh Button ***
        JButton refreshBtn = styledButton("↻ Refresh", ACCENT2.darker()); 

        rightButtons.add(sortPriorityBtn);
        rightButtons.add(sortDeadlineBtn);
        rightButtons.add(refreshBtn);

        topBar.add(leftButtons, BorderLayout.WEST);
        topBar.add(centerSearch, BorderLayout.CENTER);
        topBar.add(rightButtons, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // TABLE SECTION
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

        // ACTIONS
        sortPriorityBtn.addActionListener(e -> model.sortByPriority());
        sortDeadlineBtn.addActionListener(e -> model.sortByDeadline());

        // Connect Refresh button
        refreshBtn.addActionListener(e -> model.refresh()); 

        searchBtn.addActionListener(e -> 
            JOptionPane.showMessageDialog(this, "Search feature will be connected later.", "Search", JOptionPane.INFORMATION_MESSAGE)
        );

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

private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        b.setBorderPainted(false);
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}