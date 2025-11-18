package structures;

import structures.Task;
import java.util.Arrays; // Needed for array manipulation

public class TaskQueue {
    Task[] queue;
    int front;
    int rear;
    private static final int INITIAL_CAPACITY = 10;

    public TaskQueue() {
        // It's safer to use an initial capacity larger than 10 for real use
        queue = new Task[INITIAL_CAPACITY];
        front = -1;
        rear = -1;
    }

    public boolean isEmpty() {
        return front == -1;
    }

    public boolean isFull() {
        return (rear + 1) % queue.length == front;
    }

    /**
     * Helper method to resize the queue when it becomes full (doubles capacity).
     */
    private void resize() {
        int oldSize = queue.length;
        Task[] newQueue = new Task[oldSize * 2];
        int currentSize = size();
        
        // Copy elements from front to rear into the new array
        for (int i = 0; i < currentSize; i++) {
            newQueue[i] = queue[(front + i) % oldSize];
        }
        
        queue = newQueue;
        front = 0;
        rear = currentSize - 1;
    }

    // UPDATED: Now supports resizing when full
    public boolean enqueue(Task obj) {
        if (isFull()) {
            resize(); // Resize the array instead of just failing
        } 
        
        if (isEmpty()) {
            front = 0;
            rear = 0;
        } else {
            rear = (rear + 1) % queue.length;
        }
        queue[rear] = obj;
        return true;
    }

    // UPDATED: Returns the dequeued Task object
    public Task dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return null;
        } else {
            Task dequeuedTask = queue[front]; 
            queue[front] = null;
            
            if (front == rear) {
                // Queue becomes empty
                front = -1;
                rear = -1;
            } else {
                front = (front + 1) % queue.length;
            }
            return dequeuedTask; 
        }
    }

    /**
     * NEW: Searches the queue for a task by its ID.
     * This is required by TaskTableModel to prevent duplicate additions.
     * @param id The ID of the task to find.
     * @return The Task object if found, otherwise null.
     */
    public Task findTask(int id) {
        if (isEmpty()) return null;

        int current = front;
        do {
            if (queue[current] != null && queue[current].getId() == id) {
                return queue[current];
            }
            if (current == rear) break;
            current = (current + 1) % queue.length;
        } while (current != front);
        
        return null;
    }
    
    /**
     * NEW: Removes a specific task from the queue by ID. 
     * Necessary when a task is completed or deleted from the main list.
     * This is inefficient in a circular array, but required for functionality.
     * @param id The ID of the task to remove.
     * @return true if the task was found and removed, false otherwise.
     */
    public boolean removeTask(int id) {
        if (isEmpty()) return false;

        Task[] tempQueue = new Task[queue.length];
        int tempFront = -1;
        int tempRear = -1;
        boolean removed = false;

        // Dequeue and re-enqueue all tasks except the one to remove
        while (!isEmpty()) {
            Task currentTask = dequeue();
            if (currentTask.getId() == id) {
                removed = true;
                // Skip adding the removed task back
            } else {
                // Manually add to the temp queue (avoiding standard enqueue to manage indexes)
                if (tempFront == -1) {
                    tempFront = 0;
                    tempRear = 0;
                } else {
                    tempRear = (tempRear + 1) % tempQueue.length;
                }
                tempQueue[tempRear] = currentTask;
            }
        }

        // Restore the queue state from the temporary array
        queue = tempQueue;
        front = tempFront;
        rear = tempRear;
        
        return removed;
    }

    // NEW: Method to get all tasks in the queue for display
    public Task[] getAllTasks() {
        if (isEmpty()) {
            return new Task[0];
        }
        
        int size = size();
        Task[] tasks = new Task[size];
        int index = 0;
        int current = front;
        
        while (index < size) {
            tasks[index++] = queue[current];
            if (current == rear) break;
            current = (current + 1) % queue.length;
        }
        
        return tasks;
    }

    // NEW: Get the size of the queue
    public int size() {
        if (isEmpty()) {
            return 0;
        }
        if (rear >= front) {
            return rear - front + 1;
        } else {
            return queue.length - front + rear + 1;
        }
    }

    // NEW: Peek at the front element without removing it
    public Task peek() {
        if (isEmpty()) {
            return null;
        }
        return queue[front];
    }
}