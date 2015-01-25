package com.st.equaltests;






/**
 * Hello world!
 *
 */

class Moof {

    private int moofValue;

    Moof(int val) {
        moofValue = val;
    }

    public int getMoofValue() {
        return moofValue;
    }

    @Override
    public boolean equals(Object o) {
        if ((o instanceof Moof) && (((Moof) o).getMoofValue() == this.moofValue)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.moofValue;
        return hash;
    }
}

public class EqualsTest {

    public static void main(String[] args) {
        Moof one = new Moof(8);
        Moof two = new Moof(8);
        if (one.equals(two)) {
            System.out.println("one and two are equal");
        }
    }
}