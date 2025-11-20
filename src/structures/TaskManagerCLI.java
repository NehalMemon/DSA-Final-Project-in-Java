package structures;

import structures.SortingAndSearch;
import structures.Task;
import structures.TaskList;
import structures.TaskQueue;
import structures.TaskStack;

import java.util.Scanner;

public class TaskManagerCLI {

    // FIX 1: Changed declaration type to SortingAndSearch to expose sorting methods.
    // FIX 2: Removed redundant and uninitialized 'taskAction' field.
    private SortingAndSearch taskList; 
    
    private TaskQueue todayQueue;
    private TaskStack completedStack;
    private Scanner scanner;

    public TaskManagerCLI() {

        // The TaskList is correctly instantiated as SortingAndSearch here
        this.taskList = new SortingAndSearch(); 
        this.todayQueue = new TaskQueue();
        this.completedStack = new TaskStack();
        this.scanner = new Scanner(System.in);

        // Initial tasks are created here
        taskList.createTask(1, "Refactor DB", "Update JDBC", Task.Priority.high, Task.Status.pending, "14-11-25");
        taskList.createTask(2, "Review PRs", "Code review", Task.Priority.medium, Task.Status.pending, "12-12-25");
        taskList.createTask(3, "Write docs", "API docs", Task.Priority.medium, Task.Status.pending, "20-11-25");
        taskList.createTask(4, "Hotfix", "Payment bug", Task.Priority.high, Task.Status.pending, "02-12-25");
        taskList.createTask(5, "Brainstorm", "Q1 prep", Task.Priority.low, Task.Status.pending, "03-12-25");
    }

    public void start() {

        boolean running = true;
        System.out.println("--- Welcome to the Task Manager CLI ---");

        while (running) {
            displayMenu();
            System.out.print("\nEnter choice (1-8): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        displayAllTasks();
                        break;
                    case 2:
                        addTask();
                        break;
                    case 3:
                        searchTask();
                        break;
                    case 4:
                        sortTasks();
                        break;
                    case 5:
                        completeOrDelete();
                        break;
                    case 6:
                        displayCompletedTasks();
                        break;
                    case 7:
                        displayTodayTasks();
                        break;
                    case 8:
                        running = false;
                        System.out.println("Exiting Task Manager. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
            System.out.println("\n---");
        }
    }

    private void displayMenu() {
        System.out.println("\n\n--- MAIN MENU ---");
        System.out.println("1. View All Tasks");
        System.out.println("2. Add New Task");
        System.out.println("3. Search by Title");
        System.out.println("4. Sort Tasks");
        System.out.println("5. Mark Task Complete/Delete Task");
        System.out.println("6. View Completed Stack");
        System.out.println("7. View Today's Queue");
        System.out.println("8. Exit");
    }

    private void displayAllTasks() {
        System.out.println("\n--- PENDING TASK LIST ---");
        taskList.displayTasks();
    }

    private void searchTask() {
        System.out.print("Enter Task Title to search: ");
        String title = scanner.nextLine().trim();

        if (title.isEmpty()) {
            System.out.println("Search term cannot be empty.");
            return;
        }

        // taskList (now type SortingAndSearch) has searchByTitle
        Task foundTask = taskList.searchByTitle(title);

        if (foundTask != null) {
            System.out.println("\n--- TASK FOUND ---");
            displayTaskDetails(foundTask);
        } else {
            System.out.println("\nTask '" + title + "' was NOT found.");
        }
    }

    private void displayTaskDetails(Task task) {
        System.out.println("ID: " + task.getId());
        System.out.println("Title: " + task.getTitle());
        System.out.println("Description: " + task.getDescription());
        System.out.println("Priority: " + task.getPriority());
        System.out.println("Status: " + task.getStatus());
        System.out.println("Deadline: " + task.getDeadline());
    }

    private void addTask() {
        System.out.println("\n--- ADD NEW TASK ---");

        System.out.print("Enter new Task ID (int): ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Returning to menu.");
            return;
        }

        System.out.print("Enter Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Description: ");
        String description = scanner.nextLine();

        System.out.println("\nSelect Priority:");
        System.out.println("1. High");
        System.out.println("2. Medium");
        System.out.println("3. Low");
        System.out.print("Enter choice (1-3): ");

        Task.Priority priority;
        try {
            int priorityChoice = Integer.parseInt(scanner.nextLine());
            switch (priorityChoice) {
                case 1:
                    priority = Task.Priority.high;
                    break;
                case 2:
                    priority = Task.Priority.medium;
                    break;
                case 3:
                    priority = Task.Priority.low;
                    break;
                default:
                    System.out.println("Invalid priority choice. Defaulting to Medium.");
                    priority = Task.Priority.medium;
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid priority input. Defaulting to Medium.");
            priority = Task.Priority.medium;
        }

        System.out.print("Enter Deadline (e.g., DD-MM-YY): ");
        String deadline = scanner.nextLine();

        taskList.createTask(
                id,
                title,
                description,
                priority,
                Task.Status.pending,
                deadline);
        System.out.println("\nTask '" + title + "' added successfully with Priority: " + priority + "!");
    }

    private void sortTasks() {
        
        System.out.println("\n--- SORT TASKS ---");
        System.out.println("1. Sort by Priority (High to Low)");
        System.out.println("2. Sort by Deadline (Earliest First)");
        System.out.print("Enter choice (1/2): ");

        String choice = scanner.nextLine().trim();

        if (choice.equals("1")) {
            // FIX: Use taskList, which is correctly typed as SortingAndSearch
            taskList.sortByPriority();
            System.out.println("List sorted by Priority.");
        } else if (choice.equals("2")) {
            // FIX: Use taskList, which is correctly typed as SortingAndSearch
            taskList.sortByDeadline();
            System.out.println("List sorted by Deadline.");
        } else {
            System.out.println("Invalid sort choice.");
        }
        displayAllTasks();
    }

    private void displayCompletedTasks() {
        System.out.println("\n--- COMPLETED TASKS (STACK) ---");
        completedStack.getTasks();

    }

    private void completeOrDelete() {
        System.out.println("\n--- TASK ACTIONS ---");
        System.out.println("1. Mark Task Complete");
        System.out.println("2. Delete Task");
        System.out.print("Enter choice (1/2): ");

        try {
            int actionChoice = Integer.parseInt(scanner.nextLine());

            if (actionChoice != 1 && actionChoice != 2) {
                System.out.println("Invalid choice. Please enter 1 or 2.");
                return;
            }

            System.out.print("Enter the ID of the task to act upon: ");
            int taskId = Integer.parseInt(scanner.nextLine());

            // Need to find the task first to push a reference to the stack
            Task taskToActOn = taskList.findTask(taskId); 

            if (taskToActOn == null) {
                System.out.println("Error: Task with ID " + taskId + " not found in the list.");
                return;
            }

            if (actionChoice == 1) {
                // ACTION 1: Mark Complete (Add to Stack AND Remove from List)
                completedStack.push(taskToActOn);
                // The task must be deleted from the main taskList after being pushed to the stack
                if (taskList.deleteTask(taskId)) {
                    System.out.println("\n✅ Task ID " + taskId + " marked as **COMPLETE** and moved to the Completed Stack.");
                } else {
                     System.out.println("\nError: Task completed, but could not be removed from pending list.");
                }
            } else if (actionChoice == 2) {
                // ACTION 2: Delete Permanently
                 if (taskList.deleteTask(taskId)) {
                    System.out.println("\n🗑️ Task ID " + taskId + " permanently **DELETED**.");
                 } else {
                    System.out.println("\nError: Could not delete task.");
                 }
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number for the choice and ID.");
        }
    }

    private void displayTodayTasks() {
        System.out.println("\n--- TODAY'S TASKS (QUEUE) ---");
        todayQueue.getAllTasks();

    }
}