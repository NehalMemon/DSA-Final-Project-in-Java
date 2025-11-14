package structures;

import utils.InsertionHelper;

public class SortingAndSearch extends TaskList {

    public static void main(String[] args) {

        SortingAndSearch list = new SortingAndSearch();

        list.createTask(
                2,
                "Review pull requests",
                "Check all pending code submissions for quality assurance.",
                Task.Priority.medium,
                Task.Status.pending,
                "2025-11-12");

        list.createTask(
                1,
                "Refactor Database Connection",
                "Update deprecated JDBC calls to connection pooling.",
                Task.Priority.high,
                Task.Status.pending,
                "2025-11-15");

        list.createTask(
                3,
                "Update Documentation",
                "Write guides for new API endpoints.",
                Task.Priority.medium,
                Task.Status.pending,
                "2025-11-20");

        list.createTask(
                4,
                "Deploy Hotfix to Production",
                "Fix critical payment gateway bug immediately.",
                Task.Priority.high,
                Task.Status.pending,
                "2025-11-13");

        list.createTask(
                5,
                "Team Brainstorming Session",
                "Prepare materials for Q1 project kickoff.",
                Task.Priority.low,
                Task.Status.pending,
                "2025-11-25");

        System.out.println("\n--- BEFORE SORTING ---");
        list.displayTasks();

        list.sortByPriority();

        System.out.println("\n--- AFTER SORTING BY PRIORITY ---");
        list.displayTasks();
    }

    // Sorting method
    public void sortByPriority() {

        Task sorted = null;
        Task current = head;

        while (current != null) {
            Task nextNode = current.next;
            current.next = null;
            sorted = InsertionHelper.insertByOrder(sorted, current);
            current = nextNode;
        }

        this.head = sorted;
    }
}
    public void sortByDeadline() {

        Task sorted = null;
        Task current = head;

        while (current != null) {
            Task nextNode = current.next;
            current.next = null;
            sorted = InsertionHelper.insertByOrder(sorted, current);
            current = nextNode;
        }

        this.head = sorted;
    }
}
