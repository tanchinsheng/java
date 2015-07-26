package com.lynda.javatraining;

import com.lynda.javatraining.olives.Kalamata;
import com.lynda.javatraining.olives.Ligurio;
import com.lynda.javatraining.olives.Olive;
import com.lynda.javatraining.olives.Picholine;
import java.util.Set;
import java.util.TreeSet;

public class TreeSetMain {

    public static void main(String[] args) {

        Olive lig = new Ligurio();
        Olive kal = new Kalamata();
        Olive pic = new Picholine();
        Set<Olive> set = new TreeSet<>();
        set.add(pic);
        set.add(kal);
        set.add(lig);
        System.out.println(set);
    }

}
