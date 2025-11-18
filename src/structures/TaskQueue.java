package structures;

import structures.Task;

public class TaskQueue {
    Task[] queue;
    int front;
    int rear;

    public TaskQueue() {
        queue = new Task[10];
        front = -1;
        rear = -1;
    }

    public boolean isEmpty() {
        return front == -1;
    }

    public boolean isFull() {
        return (rear + 1) % queue.length == front;
    }

    // UPDATED: Now actually stores the Task object
    public boolean enqueue(Task obj) {
        if (isFull()) {
            System.out.println("Queue is full");
            return false;
        } else {
            if (isEmpty()) {
                front = 0;
                rear = 0;
            } else {
                rear = (rear + 1) % queue.length;
            }
            queue[rear] = obj; // ADDED: Actually store the task
            return true;
        }
    }

    // UPDATED: Returns the dequeued Task object
    public Task dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return null;
        } else {
            Task dequeuedTask = queue[front]; // ADDED: Get the task before removing
            queue[front] = null;
            
            if (front == rear) {
                // Queue becomes empty
                front = -1;
                rear = -1;
            } else {
                front = (front + 1) % queue.length;
            }
            return dequeuedTask; // ADDED: Return the task
        }
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