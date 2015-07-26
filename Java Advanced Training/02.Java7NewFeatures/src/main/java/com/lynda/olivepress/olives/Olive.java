package com.lynda.olivepress.olives;

public class Olive {

    private String oliveName = "Kalamata";
    private int color = 0x000000;

    public Olive() {
    }

    public Olive(String oliveName) {
        this.oliveName = oliveName;
    }

    public String getOliveName() {
        return oliveName;
    }

    public int getColor() {
        return color;
    }

    /**
     *
     * @param oliveName
     * @param color
     */
    public Olive(String oliveName, int color) {
        this(oliveName);
        this.color = color;
    }

    @Override
    public String toString() {
        return "name: " + this.oliveName + ": " + "color: " + this.color;
    }

}
