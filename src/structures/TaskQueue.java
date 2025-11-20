package structures;

import structures.Task;
import java.util.Arrays; 

public class TaskQueue {
    Task[] queue;
    int front;
    int rear;
    

    public TaskQueue() {
        // It's safer to use an initial capacity larger than 10 for real use
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

  
    private void resize() {
        int oldSize = queue.length;
        Task[] newQueue = new Task[oldSize * 2];
        int currentSize = size();
        
      
        for (int i = 0; i < currentSize; i++) {
            newQueue[i] = queue[(front + i) % oldSize];
        }
        
        queue = newQueue;
        front = 0;
        rear = currentSize - 1;
    }


    public boolean enqueue(Task obj) {
        if (isFull()) {
            resize(); 
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
  
    public Task dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return null;
        } else {
            Task dequeuedTask = queue[front]; 
            queue[front] = null;
            
            if (front == rear) {
                front = -1;
                rear = -1;
            } else {
                front = (front + 1) % queue.length;
            }
            return dequeuedTask; 
        }
    }

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


    public Task peek() {
        if (isEmpty()) {
            return null;
        }
        return queue[front];
    }
}