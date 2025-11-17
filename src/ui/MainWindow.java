package ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import structures.Task;
import structures.TaskList;

public class MainWindow extends JFrame {

    private JTable taskTable;
    private JButton addTaskBtn, viewCompletedBtn, viewTodayBtn;

    private TaskList taskList;
    // FIX 1: Declare TaskTableModel and AddTaskForm as fields
    private TaskTableModel model;
    private AddTaskForm addTaskForm; 

    public MainWindow(TaskList list) {
        this.taskList = list;

        setTitle("Study Manager");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---------- TOP BUTTON BAR ----------
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        addTaskBtn = new JButton("➕ Add Task");
        viewCompletedBtn = new JButton("✔ Completed");
        viewTodayBtn = new JButton("📅 Today");

        topPanel.add(addTaskBtn);
        topPanel.add(viewCompletedBtn);
        topPanel.add(viewTodayBtn);

        add(topPanel, BorderLayout.NORTH);

        // ---------- TABLE SETUP ----------
        // FIX 2: Initialize the model using the list
        model = new TaskTableModel(taskList); 
        taskTable = new JTable(model);

        taskTable.setRowHeight(35);
        
        // Use the custom row renderer for all cells to color the row
        taskTable.setDefaultRenderer(Object.class, new TaskRowRenderer(model));

        // Get the Actions column index (it is the last one: 3)
        TableColumn actionColumn = taskTable.getColumnModel().getColumn(3);
        
        // Set the custom renderer for the Action column
        actionColumn.setCellRenderer(new TaskActionRenderer());
        
        // Set the custom editor for the Action column to handle clicks
        actionColumn.setCellEditor(new TaskActionEditor(model, taskTable));
        
        // Set column widths (Optional, but makes it look much better)
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(300); // Title
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(80);  // Priority
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Deadline
        actionColumn.setPreferredWidth(100);                           // Actions

        JScrollPane scroll = new JScrollPane(taskTable);
        add(scroll, BorderLayout.CENTER);

        // ---------- BUTTON LISTENERS ----------
        
        // FIX 3: Initialize the AddTaskForm once, passing the data list and the table model
        addTaskForm = new AddTaskForm(taskList, model);
        
        // FIX 4: Wire up the button to make the AddTaskForm visible
        addTaskBtn.addActionListener(e -> {
            addTaskForm.setVisible(true);
        });

        // Placeholder listeners for other buttons (keep for future use)
        // viewCompletedBtn.addActionListener(e -> new CompletedTasksWindow(taskList).setVisible(true));
        // viewTodayBtn.addActionListener(e -> new TodayTasksWindow(taskList).setVisible(true));
    }
}