package com.lynda.olivepress.olives;

import java.util.ArrayList;

public class EnumOliveJar {

    public ArrayList<EnumOlive> olives;

    {
        System.out.println("initializing...");
        olives = new ArrayList<>();
        olives.add(new EnumOlive(EnumOliveName.GOLDEN, 0xDA9100));
    }

    public EnumOliveJar() {
        System.out.println("Constructor...");
    }

    public EnumOliveJar(int nOlives, EnumOliveName oliveName, int color) {
        for (int i = 1; i <= nOlives; i++) {
            olives.add(new EnumOlive(oliveName, color));
        }
    }

    public void addOlive(EnumOliveName oliveName, int color) {
        olives.add(new EnumOlive(oliveName, color));
    }

    public void reportOlives() {

        new Object() {
            public void open() {
                System.out.println("Twist, twist, twist...");
                System.out.println("Pop!");
            }
        }.open();

        for (EnumOlive o : olives) {
            System.out.println("It's a " + o.getOliveName() + " olive!!");
        }
    }

}
