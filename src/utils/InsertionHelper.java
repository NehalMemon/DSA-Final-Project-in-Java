package utils;

import utils.PriorityHelper;
import structures.Task;

public class InsertionHelper {
    public static Task insertByOrder(Task sortedHead, Task node) {
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
}
