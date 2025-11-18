package structures;

/**
 * Represents a single task with status, priority, and deadline.
 * Acts as a node in the TaskList (linked list) structure via the 'next' field.
 */
public class Task {
    public enum Status {
        pending, completed
    }

    public enum Priority {
        high, medium, low
    }

    private int id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private String deadline;

    public Task next; // Pointer for the linked list (TaskList)

    public Task(int id, String title, String description, Priority priority, Status status, String deadline) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.deadline = deadline;
        this.next = null;
    }

    // --- Getters ---
    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public Status getStatus() {
        return this.status;
    }

    public String getDeadline() {
        return this.deadline;
    }

    // --- Setters ---
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}