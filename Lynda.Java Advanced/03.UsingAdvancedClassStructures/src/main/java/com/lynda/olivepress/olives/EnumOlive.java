package com.lynda.olivepress.olives;

public class EnumOlive {

    private EnumOliveName oliveName;
    private int color = 0x000000;

    private EnumOlive() {
    }

    public EnumOlive(EnumOliveName oliveName) {
        this.oliveName = oliveName;
    }

    public EnumOliveName getOliveName() {
        return oliveName;
    }

    /**
     *
     * @param oliveName
     * @param color
     */
    public EnumOlive(EnumOliveName oliveName, int color) {
        this(oliveName);
        this.color = color;
    }

    @Override
    public String toString() {
        return "name: " + this.oliveName + ": " + "color: " + this.color;
    }

}
