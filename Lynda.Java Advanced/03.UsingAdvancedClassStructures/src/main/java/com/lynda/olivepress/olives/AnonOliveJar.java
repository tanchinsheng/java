package com.lynda.olivepress.olives;

import java.util.ArrayList;

public class AnonOliveJar {

    public ArrayList<Olive> olives;

    {
        System.out.println("initializing...");
        olives = new ArrayList<>();
        olives.add(new Olive("Golden", 0xDA9100));
    }

    public AnonOliveJar() {
        System.out.println("Constructor...");
    }

    public AnonOliveJar(int nOlives, String oliveName, int color) {
        for (int i = 1; i <= nOlives; i++) {
            olives.add(new Olive(oliveName, color));
        }
    }

    public void addOlive(String oliveName, int color) {
        olives.add(new Olive(oliveName, color));
    }

    public void reportOlives() {
//        class JarLid {
//
//            public void open() {
//                System.out.println("Twist, twist, twist...");
//                System.out.println("Pop!");
//            }
//        }
//        new JarLid().open();
        new Object() {
            public void open() {
                System.out.println("Twist, twist, twist...");
                System.out.println("Pop!");
            }
        }.open();
        for (Olive o : olives) {
            System.out.println("It's a " + o.oliveName + " olive!!");
        }
    }

    class Olive {

        public static final int BLACK = 0x000000;

        private String oliveName;
        private int color = BLACK;

        public Olive() {
        }

        public Olive(String oliveName) {
            this.oliveName = oliveName;
        }

        public Olive(String oliveName, int color) {
            this(oliveName);
            this.color = color;
        }

        @Override
        public String toString() {
            return "name: " + this.oliveName + ": " + "color: " + this.color;
        }

    }

}
