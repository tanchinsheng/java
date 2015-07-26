package com.lynda.olivepress.olives;

public class Olive {

    private String oliveName;
    private int color = 0x000000;

    private Olive() {
    }

    public Olive(String oliveName) {
        this.oliveName = oliveName;
    }

    public Olive(String oliveName, int color) {
        this(oliveName);
        this.color = color;
    }

    public String getOliveName() {
        return oliveName;
    }

    @Override
    public String toString() {
        return "name: " + this.oliveName + ": " + "color: " + this.color;
    }

}
