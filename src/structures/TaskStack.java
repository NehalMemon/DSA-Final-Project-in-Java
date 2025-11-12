package structures;



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
}
