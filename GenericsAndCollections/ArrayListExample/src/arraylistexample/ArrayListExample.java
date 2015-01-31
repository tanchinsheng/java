/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package arraylistexample;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cstan
 */
public class ArrayListExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        List<Integer> numbers = new ArrayList<Integer>();
        
        // Adding
        numbers.add(10);
        numbers.add(100);
        numbers.add(40);
        
        // Retreving 
        System.out.println("First Element:");
        System.out.println(numbers.get(0));

        System.out.println("1st Iteration:");
        for (int i = 0; i < numbers.size(); i++) {
            System.out.println(numbers.get(i));
        }

        // This is VERY SLOW
        numbers.remove(0);
        
        System.out.println("2nd Iteration:");
        for (Integer value : numbers) {
            System.out.println(value);
        }
        
        // Removing items (careful)
        numbers.remove(numbers.size() - 1);

        System.out.println("3nd Iteration:");
        for (Integer value : numbers) {
            System.out.println(value);
        }

        // Removing items (careful)
        numbers.remove(0);

        System.out.println("4nd Iteration:");
        for (Integer value : numbers) {
            System.out.println(value);
        }
        
        numbers.remove(0);

        System.out.println("5nd Iteration:");
        for (Integer value : numbers) {
            System.out.println(value);
        }
        
        //List Interface...
        List<String> values = new ArrayList<String>();
        
    }
}
