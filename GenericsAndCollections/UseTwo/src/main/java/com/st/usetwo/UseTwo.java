package com.st.usetwo;

public class UseTwo<T, X> {

    T one;
    X two;

    UseTwo(T one, X two) {
        this.one = one;
        this.two = two;
    }

    T getT() {
        return one;
    }

    X getX() {
        return two;
    }
// test it by creating it with <String, Integer>
    public static void main(String[] args) {
        UseTwo<String, Integer> twos = new UseTwo<String, Integer>("foo", 42);
        String theT = twos.getT(); // returns a String
        int theX = twos.getX(); // returns Integer, unboxes to int
    }
}