package structures;

public class Task {
    enum Status {
        pending, completed
    }

    enum Priority {
        high, medium, low
    }

    private int id;
    private String description;
    private Priority priority;
    private Status status;
    private String deadline;

    public Task next;

    public Task(int id, String description, Priority priority, Status status, String deadline) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.deadline = deadline;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return this.priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDeadline() {
        return this.deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
