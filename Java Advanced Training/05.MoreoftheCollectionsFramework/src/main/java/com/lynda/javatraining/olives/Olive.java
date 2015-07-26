package com.lynda.javatraining.olives;

public class Olive implements Comparable<Olive> {

    private OliveName oliveName;
    private OliveColor color;

    public Olive() {
    }

    public Olive(OliveName oliveName, OliveColor color) {
        this.oliveName = oliveName;
        this.color = color;
    }

    public OliveName getOliveName() {
        return oliveName;
    }

    @Override
    public String toString() {
        return "oliveName: " + this.oliveName.toString()
                + ", color: " + this.color.toString();
    }

    @Override
    public int compareTo(Olive o) {
//        String s1 = this.getClass().getSimpleName();
//        String s2 = o.getClass().getSimpleName();
        String s1 = this.oliveName.toString();
        String s2 = o.oliveName.toString();
        return s1.compareTo(s2);

    }

}
