package com.st.testlegacy;

// a Java 5 class using a generic collection
import java.util.*;

class Adder {

    int addAll(List list) {
// method with a non-generic List argument,
// but assumes (with no guarantee) that it will be Integers
        Iterator it = list.iterator();
        int total = 0;
        while (it.hasNext()) {
            int i = ((Integer) it.next()).intValue();
            total += i;
        }
        return total;
    }
}

public class TestLegacy {

    public static void main(String[] args) {
        List<Integer> myList = new ArrayList<Integer>();
// type safe collection
        myList.add(4);
        myList.add(6);
        Adder adder = new Adder();
        int total = adder.addAll(myList);
// pass it to an untyped argument
        System.out.println(total);
    }
}
