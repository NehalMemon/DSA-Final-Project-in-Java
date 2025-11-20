package structures;

import utils.InsertionHelper;

public class SortingAndSearch extends TaskList {

    public void sortByPriority() {

        Task sorted = null;
        Task current = head;

        while (current != null) {
            Task nextNode = current.next;
            current.next = null;
            sorted = InsertionHelper.insertByPriority(sorted, current);
            current = nextNode;
        }

        this.head = sorted;
    }

    public void sortByDeadline() {

        Task sorted = null;
        Task current = head;

        while (current != null) {
            Task nextNode = current.next;
            current.next = null;
            sorted = InsertionHelper.insertByDeadline(sorted, current);
            current = nextNode;
        }

        this.head = sorted;
    }

     public Task searchByTitle(String title){
        Task current = head;
        while(current != null){
            if(current.getTitle().equalsIgnoreCase(title)){
                return current;
            }
            current = current.next;
        }
        return null;
    }
}
