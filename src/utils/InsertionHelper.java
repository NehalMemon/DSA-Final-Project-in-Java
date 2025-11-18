package utils;

import utils.PriorityHelper;
import utils.DateHelper;
import structures.Task;

public class InsertionHelper {
    public static Task insertByPriority(Task sortedHead, Task node) {
        if (sortedHead == null ||
                PriorityHelper.PriorityAssign(node) < PriorityHelper.PriorityAssign(sortedHead)) {
            node.next = sortedHead;
            return node;
        }
        Task current = sortedHead;
        while (current.next != null &&
                PriorityHelper.PriorityAssign(current.next) <= PriorityHelper.PriorityAssign(node)) {
            current = current.next;
        }
        node.next = current.next;
        current.next = node;

        return sortedHead;
    }

    public static Task insertByDeadline(Task sortedHead, Task node) {

        long nodeDate = DateHelper.dateAsInt(node.getDeadline());
    
        // Case 1: Insert at the head (smallest date first)
        if (sortedHead == null ||
            nodeDate < DateHelper.dateAsInt(sortedHead.getDeadline())) {
    
            node.next = sortedHead;
            return node;
        }
    
        Task current = sortedHead;
    
        // Case 2: Find correct insertion point
        while (current.next != null &&
               DateHelper.dateAsInt(current.next.getDeadline()) <= nodeDate) {
    
            current = current.next;
        }
    
        // // Safe debug print
        // if (current.next != null) {
        //     System.out.println(
        //             DateHelper.dateAsInt(current.next.getDeadline()) +
        //             "   " +
        //             nodeDate
        //     );
        // }
    
        // Case 3: Insert node
        node.next = current.next;
        current.next = node;
    
        return sortedHead;
    }
    
    public static void main(String[] args) {

    }

}
