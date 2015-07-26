package com.lynda.javatraining;

import com.lynda.olivepress.olives.Olive;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        Olive o1 = new Olive("Kalamata", 0x000000);
        Olive o2 = new Olive("Picholine", 0x00FF00);
        Olive o3 = new Olive("Ligurio", 0x000000);

        Olive[] olives = {o1, o2, o3};
        System.out.println(Arrays.toString(olives));

        List<Olive> o = new ArrayList<>();
        o.add(o1);
        o.add(o2);
        o.add(o3);
        System.out.println(o);

        for (Olive i : o) {
            System.out.println(i.getOliveName() + ":" + i.getColor());
        }
    }

}
