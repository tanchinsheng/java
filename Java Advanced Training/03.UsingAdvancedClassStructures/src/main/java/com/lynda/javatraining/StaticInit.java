package com.lynda.javatraining;

import com.lynda.olivepress.olives.Olive;
import com.lynda.olivepress.olives.StaticOliveJar;
import java.util.List;

public class StaticInit {

    public static void main(String[] args) throws Exception {
        System.out.println("Starting app...");
        List<Olive> olives = StaticOliveJar.olives;
        for (Olive o : olives) {
            System.out.println("It's a " + o.getOliveName());
        }
    }

}
