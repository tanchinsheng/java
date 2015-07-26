package com.lynda.javatraining;

import com.lynda.javatraining.olives.Kalamata;
import com.lynda.javatraining.olives.Ligurio;
import com.lynda.javatraining.olives.Olive;
import com.lynda.javatraining.olives.Picholine;
import java.util.HashSet;

public class HashSetMain {

    public static void main(String[] args) {
        Olive pic = new Picholine();
        Olive lig = new Ligurio();
        Olive kal = new Kalamata();

        HashSet<Olive> set = new HashSet<>();
        set.add(pic);
        set.add(kal);
        set.add(lig);
        System.out.println(set);
        System.out.println("There are " + set.size() + " olives in the set.");
        set.add(pic);
        System.out.println("There are " + set.size() + " olives in the set.");
        set.add(kal);
        System.out.println("There are " + set.size() + " olives in the set.");
        set.add(null);
        System.out.println("There are " + set.size() + " olives in the set.");
        set.remove(lig);
        System.out.println("There are " + set.size() + " olives in the set.");
    }

}
