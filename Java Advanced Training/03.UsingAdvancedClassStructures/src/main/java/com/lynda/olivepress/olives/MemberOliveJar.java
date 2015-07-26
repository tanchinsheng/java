package com.lynda.olivepress.olives;

import java.util.ArrayList;

public class MemberOliveJar {

    public ArrayList<MemberOlive> olives;

    {
        System.out.println("initializing...");
        olives = new ArrayList<>();
        olives.add(new MemberOlive("Golden", 0xDA9100));
    }

    public MemberOliveJar() {
        System.out.println("Constructor...");
    }

    public MemberOliveJar(int nOlives, String oliveName, int color) {
        for (int i = 1; i <= nOlives; i++) {
            olives.add(new MemberOlive(oliveName, color));
        }
    }

    public void addOlive(String oliveName, int color) {
        olives.add(new MemberOlive(oliveName, color));
    }

    public void reportOlives() {
        for (MemberOlive olive : olives) {
            System.out.println("It's a " + olive.getOliveName() + " olive!");
        }

    }
}

class MemberOlive {

    private String oliveName;
    private int color = 0x000000;

    private MemberOlive() {
    }

    public MemberOlive(String oliveName) {
        this.oliveName = oliveName;
    }

    public String getOliveName() {
        return oliveName;
    }

    /**
     *
     * @param oliveName
     * @param color
     */
    public MemberOlive(String oliveName, int color) {
        this(oliveName);
        this.color = color;
    }

    @Override
    public String toString() {
        return "name: " + this.oliveName + ": " + "color: " + this.color;
    }

}
