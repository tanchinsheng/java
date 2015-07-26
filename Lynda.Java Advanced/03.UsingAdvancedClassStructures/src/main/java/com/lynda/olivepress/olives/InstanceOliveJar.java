package com.lynda.olivepress.olives;

import java.util.ArrayList;

public class InstanceOliveJar {

    /**
     *
     */
    public ArrayList<Olive> olives;

    {
        System.out.println("initializing...");
        olives = new ArrayList<>();
        olives.add(new Olive("Golden", 0xDA9100));
    }

    public InstanceOliveJar() {
        System.out.println("Constructor...");
    }

    public InstanceOliveJar(int nOlives, String oliveName, int color) {
        for (int i = 1; i <= nOlives; i++) {
            olives.add(new Olive(oliveName, color));

        }
    }
}
