package structures;

import structures.TaskList;

public class SortingAndSearch {
    public static void main(String[] args) {

        TaskList list = new TaskList();
        list.createTask(
                2,
                "Review pull requests",
                "Check all pending code submissions for quality assurance.", // New: Description
                Task.Priority.medium,
                Task.Status.pending,
                "2025-11-12");
        list.createTask(
                1,
                "Refactor Database Connection",
                "Update all deprecated JDBC calls to use modern connection pooling standards.",
                Task.Priority.high,
                Task.Status.pending,
                "2025-11-15");

        list.createTask(
                3,
                "Update Documentation",
                "Write comprehensive guides for the new API endpoints and internal components.",
                Task.Priority.medium,
                Task.Status.pending,
                "2025-11-20");

        list.createTask(
                4,
                "Deploy Hotfix to Production",
                "Address the critical bug reported in the payment gateway immediately.",
                Task.Priority.high,
                Task.Status.pending,
                "2025-11-13");

        list.createTask(
                5,
                "Team Brainstorming Session",
                "Schedule and prepare materials for the Q1 project kick-off meeting.",
                Task.Priority.low,
                Task.Status.pending,
                "2025-11-25");

        list.displayTasks();        
    }

}