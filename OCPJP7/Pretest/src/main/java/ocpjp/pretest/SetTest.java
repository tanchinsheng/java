package ocpjp.pretest;

import java.util.*;
import java.util.concurrent.*;

class SetTest {

    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(10, 5, 10, 20); // LINE A
        //List is unsorted.
        System.out.println(list);
        //HashSet is unsorted and retains unique elements.
        System.out.println(new HashSet<Integer>(list));
        //TreeSet is sorted and retains unique elements.
        System.out.println(new TreeSet<Integer>(list));
        //ConcurrentSkipListSet is sorted and retains unique elements.
        System.out.println(new ConcurrentSkipListSet<Integer>(list));
    }
}