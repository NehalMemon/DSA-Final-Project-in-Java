package ui;

import structures.Task;
import structures.TaskList;
import javax.swing.SwingUtilities;

public class MainApp {

    public static void main(String[] args) {
        
        // 1. Initialize the Data Model
        TaskList taskList = createInitialTaskList();
        
        // 2. Launch the UI on the Event Dispatch Thread (Swing requirement)
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow(taskList);
            window.setVisible(true);
        });
    }

    private static TaskList createInitialTaskList() {
        TaskList list = new TaskList();
        
        // Add initial tasks with various priorities
        list.createTask(
                1,
                "Refactor Database Connection",
                "Update deprecated JDBC calls.",
                Task.Priority.high,
                Task.Status.pending,
                "2025-11-15");

        list.createTask(
                2,
                "Review Pull Requests",
                "Check pending code submissions.",
                Task.Priority.medium,
                Task.Status.pending,
                "2025-11-12");

        list.createTask(
                3,
                "Update Documentation",
                "Write comprehensive API guides.",
                Task.Priority.medium,
                Task.Status.pending,
                "2025-11-20");

        list.createTask(
                4,
                "Deploy Hotfix to Production",
                "Address the critical payment gateway bug.",
                Task.Priority.high,
                Task.Status.pending,
                "2025-11-13");

        list.createTask(
                5,
                "Team Brainstorming Session",
                "Schedule Q1 project kick-off.",
                Task.Priority.low,
                Task.Status.pending,
                "2025-11-25");
        
        return list;
    }
}