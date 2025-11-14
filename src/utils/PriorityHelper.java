package utils;

public class PriorityHelper {

    public static int PriorityAssign(String priority){
        if(priority.equalsIgnoreCase("high")){
            return 1;
        }

        if(priority.equalsIgnoreCase("medium")){
            return 2;
        }

        if(priority.equalsIgnoreCase("low")){
            return 3;
        }
        return 0;
    }
}

