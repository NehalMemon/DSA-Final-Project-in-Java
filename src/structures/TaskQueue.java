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
        return rear == front;
    }

    public boolean isFull() {
        return (rear + 1) % queue.length == front;
    }

    public boolean enqueue(Task obj) {
        if (isFull()) {
            System.out.println("False");

            return false;
        } else {
            if (isEmpty()) {
                front++;
            }
            rear = (rear + 1) % queue.length;
            System.out.println("True");

            return true;
        }
    }

    public boolean dequeue() {
        if (isEmpty()) {
            System.out.println("False");
            return false;
        } else {
            queue[front] = null;
            if (front == rear) {
                front--;
                rear--;
            } else {
                front = (front + 1) % queue.length;
            }
            System.out.println("True");
            return true;
        }
    }
}
