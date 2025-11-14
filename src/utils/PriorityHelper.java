package utils;

import structures.Task;


public class PriorityHelper {

    public static int PriorityAssign(Task task){
        switch (task.getPriority()){
            case high:
                return 1;
            case medium:
                return 2;
            default:
                return 1;
        }
    }
}

