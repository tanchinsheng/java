
import java.util.LinkedList;

public class NameQueue {

    public static void main(String[] args) {
        LinkedList<String> names = new LinkedList<>();
        names.addLast("Cynthia");
        names.addLast("Raymond");
        names.addLast("David");
        for (String name : names) {
            System.out.println(name);
        }
        System.out.println();
        names.removeFirst();
        for (String name : names) {
            System.out.println(name);
        }
        System.out.println();
        names.addLast("Clayton");
        for (String name : names) {
            System.out.println(name);
        }
        System.out.println("Size of queue: " + names.size());
        if (!names.isEmpty()) {
            names.removeFirst();
        }
        System.out.println("Size of queue: " + names.size());
        for (String name : names) {
            System.out.println(name);
        }
    }

}
