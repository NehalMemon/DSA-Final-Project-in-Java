package structures;

import structures.Task;

public class TaskList {

    protected Task head;

    public TaskList() {
        this.head = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    // Original method - creates task with all parameters
    public void createTask(int id, String title, String description, Task.Priority priority, Task.Status status,
            String deadline) {
        Task newTask = new Task(id, title, description, priority, status, deadline);
        if (isEmpty()) {
            head = newTask;
        } else {
            Task current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newTask;
        }
    }

    // NEW: Overloaded method to accept Task object directly (needed for undo operation)
    public void createTask(Task task) {
        if (isEmpty()) {
            head = task;
            task.next = null; // Ensure the task's next is null when added
        } else {
            Task current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = task;
            task.next = null; // Ensure no dangling references
        }
    }

    public boolean deleteTask(int id) {
        if (isEmpty()) {
            return false;
        }
        if (head.getId() == id) {
            head = head.next;
            return true;
        }

        Task current = head;
        Task prev = null;

        while (current != null && current.getId() != id) {
            prev = current;
            current = current.next;
        }
        if (current == null) {
            return false;
        }
        prev.next = current.next;
        return true;
    }

    // FIXED: Simplified the logic
    public Task findTask(int id) {
        Task current = head;
        while (current != null) {
            if (current.getId() == id) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    public boolean markComplete(int id) {
        Task completedTask = findTask(id);
        if (completedTask != null) {
            completedTask.setStatus(Task.Status.completed);
            return true;
        }
        return false;
    }

    public void displayTasks() {
        Task current = head;
        while (current != null) {
            if (current.getStatus() == Task.Status.pending) {
                System.out.println("ID: " + current.getId() + ", Title: " + current.getTitle() + 
                        ", Description: " + current.getDescription() +
                        ", Priority: " + current.getPriority() + ", Status: " + current.getStatus() +
                        ", Deadline: " + current.getDeadline());
                System.out.println();
            }
            current = current.next;
        }
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
    
    public int size() {
        Task current = head;
        int size = 0;
        while (current != null) {
            current = current.next;
            size++;
        }
        return size;
    }

    public Task get(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("Index cannot be negative");
        }
        Task current = head;
        int count = 0;
        while (current != null) {
            if (count == index) {
                return current;
            }
            count++;
            current = current.next;
        }
        throw new IndexOutOfBoundsException("Index " + index + " out of bounds");
    }
}