package part1arraylist;

/**
The first part of a series on the Java Collections Framework, an absolutely vital set of classes for 
* organising data in your code. In this part we’ll look at ArrayList; an expandable array. 
* ArrayList is probably the most used and easiest to use member of the collections framework.
 */
import java.util.ArrayList;
import java.util.List;
 
public class App {
 
    public static void main(String[] args) {
        ArrayList<Integer> numbers = new ArrayList<>();
 
        // Adding
        numbers.add(10);
        numbers.add(100);
        numbers.add(40);
 
        // Retrieving
        System.out.println(numbers.get(0));
 
        System.out.println("nIteration #1: ");
        // Indexed for loop iteration
        for (int i = 0; i < numbers.size(); i++) {
            System.out.println(numbers.get(i));
        }
 
        // Removing items (careful!)
        numbers.remove(numbers.size() - 1);
 
        // This is VERY slow
        numbers.remove(0);
 
        System.out.println("nIteration #2: ");
        for (Integer value : numbers) {
            System.out.println(value);
        }
 
        // List interface ...
        List<String> values = new ArrayList<>();
    }
}
