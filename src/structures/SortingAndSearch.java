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
                Task.Status.completed,
                "2025-11-12");
    }

}