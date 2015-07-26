package com.lynda.javatraining.olives;

public class Olive {

    private final OliveName oliveName;
    private final OliveColor color;

    public Olive(OliveName oliveName, OliveColor color) {
        this.oliveName = oliveName;
        this.color = color;
    }

    @Override
    public String toString() {
        return "Olive{" + "oliveName=" + oliveName + ", color=" + color.toString() + '}';
    }

}
