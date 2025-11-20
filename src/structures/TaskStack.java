package structures;

import java.util.ArrayList;
import java.util.List;

public class TaskStack {
    Task[] stack;
    int top;

    public TaskStack(){
        this.stack = new Task[100];
        this.top = -1;
    }

    public boolean isEmpty(){
        return top == -1;
    }

    public int size(){
        return top + 1;
    }

    public void push(Task obj){
        top++;
        stack[top] = obj;
    }

    public Task pop(){
        if(isEmpty()){
            System.out.println("stack is underflow");
            return null;
        }
       Task poppedTask =  stack[top];
       stack[top] = null;
       top--;
       return poppedTask;
    }

    public Task[] getTasks(){
        if(size()==0){
            return new Task[0];
        }
        Task[] displayList = new Task[size()];
        int index = 0;

        for(int i = top; i >= 0; i--){
          displayList[index] = stack[i];
          index ++;
        }
        return displayList;
    }
}
