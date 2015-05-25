/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package charsequence;
// CharSequenceDemo presents a String value -- backwards.

public class CharSequenceDemo implements CharSequence {

    private final String s;

    public CharSequenceDemo(String s) {
        //It would be much more efficient to just reverse the string
        //in the constructor. But a lot less fun!
        this.s = s;
    }

    @Override
    public int length() {
        return s.length();
    }

    @Override
    public char charAt(int index) {
        if ((index < 0) || (index >= s.length())) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return s.charAt(fromEnd(index));
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        if (start < 0) {
            throw new StringIndexOutOfBoundsException(start);
        }
        if (end > s.length()) {
            throw new StringIndexOutOfBoundsException(end);
        }
        if (start > end) {
            throw new StringIndexOutOfBoundsException(start - end);
        }
        StringBuilder sub = new StringBuilder(s.subSequence(fromEnd(end), fromEnd(start)));
        return sub.reverse().toString();
    }

    //If the string is backwards, the end is the beginning!
    private int fromEnd(int i) {
        return s.length() - 1 - i;
    }

    @Override
    public String toString() {
        StringBuilder sub = new StringBuilder(this.s);
        return sub.reverse().toString();
    }

    //Random int from 0 to max. As random() generates values between 0 and 0.9999
    private static int random(int max) {
        return (int) Math.round(Math.random() * (max + 1));
    }

    public static void main(String[] args) {
        CharSequenceDemo s
                = new CharSequenceDemo("Write a class that implements the CharSequence "
                        + "interface found in the java.lang package.");

        System.out.println("The object's class is " + s.getClass().getSimpleName() + " in "
                + s.getClass().getPackage().getName());
        s.getClass().getPackage().getName();
        //exercise charAt() and length()
        for (int i = 0; i < s.length(); i++) {
            System.out.print(s.charAt(i));
        }
        System.out.println("");

        //exercise subSequence() and length();
        int start = random(s.length() - 1);
        int end = random(s.length() - 1 - start) + start;
        System.out.println(s.subSequence(start, end));

        //exercise toString();
        System.out.println(s);
    }

}
