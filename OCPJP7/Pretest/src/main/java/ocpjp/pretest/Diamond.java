package ocpjp.pretest;

import java.util.*;

class Diamond {

    public static void main(String[] args) {
        
        // When executed, the program prints the following: 1 2.0 3.0.
        // The List is a generic type that is used here in raw form; hence it allows us to put different types of values in list2.
        // Therefore, it prints the following: 1 2.0 3.0.
       
        List list1 = new ArrayList<>(Arrays.asList(1, "two", 3.0)); // ONE
        List list2 = new LinkedList<>(Arrays.asList(new Integer(1), new Float(2.0F), new Double(3.0))); // TWO
        list1 = list2; // THREE
        for (Object element : list1) {
            System.out.print(element + " ");
        }
    }
}