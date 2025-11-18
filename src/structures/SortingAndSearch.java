package structures;

import utils.InsertionHelper;

public class SortingAndSearch extends TaskList {

    public void sortByPriority() {

        Task sorted = null;
        Task current = head;

        while (current != null) {
            Task nextNode = current.next;
            current.next = null;
            sorted = InsertionHelper.insertByPriority(sorted, current);
            current = nextNode;
        }

        this.head = sorted;
    }

    public void sortByDeadline() {

        Task sorted = null;
        Task current = head;

        while (current != null) {
            Task nextNode = current.next;
            current.next = null;
            sorted = InsertionHelper.insertByDeadline(sorted, current);
            current = nextNode;
        }

        this.head = sorted;
    }

     public Task searchByTitle(String title){
        Task current = head;
        while(current != null){
            if(current.getTitle().equalsIgnoreCase(title)){
                return current;
            }
            current = current.next;
        }
        return null;
    }
}
// public static void main(String[] args) {

// SortingAndSearch list = new SortingAndSearch();

// list.createTask(
// 2,
// "Review pull requests",
// "Check all pending code submissions for quality assurance.",
// Task.Priority.medium,
// Task.Status.pending,
// "12-12-25");

// list.createTask(
// 1,
// "Refactor Database Connection",
// "Update deprecated JDBC calls to connection pooling.",
// Task.Priority.high,
// Task.Status.pending,
// "14-11-25");

// list.createTask(
// 3,
// "Update Documentation",
// "Write guides for new API endpoints.",
// Task.Priority.medium,
// Task.Status.pending,
// "20-11-25");

// list.createTask(
// 4,
// "Deploy Hotfix to Production",
// "Fix critical payment gateway bug immediately.",
// Task.Priority.high,
// Task.Status.pending,
// "02-12-25");

// list.createTask(
// 5,
// "Team Brainstorming Session",
// "Prepare materials for Q1 project kickoff.",
// Task.Priority.low,
// Task.Status.pending,
// "03-12-25");

// // System.out.println("\n--- BEFORE SORTING ---");
// // list.displayTasks();

// // list.sortByPriority();

// // System.out.println("\n--- AFTER SORTING BY PRIORITY ---");
// list.displayTasks();

// list.sortByDeadline();

// System.out.println("\n--- AFTER SORTING BY DEADLINE ---");
// list.displayTasks();
// }
