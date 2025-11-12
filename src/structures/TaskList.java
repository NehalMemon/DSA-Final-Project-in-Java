package structures;

import structures.Task;

public class TaskList {

    private Task head;

    public TaskList() {
        this.head = null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public void createTask(int id, String title, String description, Task.Priority priority, Task.Status status,
            String deadline) {
        Task newTask = new Task(id, description, priority, status, deadline);
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
            if (current == null) {
                return false;
            }
        }
        prev.next = current.next;
        return true;
    }

    public Task findTask(int id) {
        Task current = head;
        while (current.getId() != id) {
            current = current.next;
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    public Task markComplete(int id) {
        Task completedTask = findTask(id);
        if (completedTask != null) {
            completedTask.setStatus(Task.Status.completed);
        }
        return completedTask;
    }

    public void displayTasks() {
        Task current = head;
        while (current != null) {
            if (current.getStatus() == Task.Status.pending) {
                System.out.println("ID: " + current.getId() + ", Description: " + current.getDescription() +
                        ", Priority: " + current.getPriority() + ", Status: " + current.getStatus() +
                        ", Deadline: " + current.getDeadline());
                current = current.next;
            } else {
                current = current.next;
            }
        }
    }

    public int Size() {
        Task current = head;
        int size = 0;
        while (current != null) {
            current = current.next;
            size++;
        }
        return size;
    }
}
