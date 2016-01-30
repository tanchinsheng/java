/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q573.string;

/**
 *
 * What will it print when compiled and run?
 */
public class StringFromChar {

    /**
     * Since newStr is null at the beginning, the first iteration of the loop
     * assigns "nullg" to newStr. Therefore, at the end of the loop, myStr is
     * actually "nullgood". Had newStr been defined as String newStr = ""; then
     * the program would have printed false for newStr == myStr because both the
     * references are pointing to two different objects, and true for
     * newStr.equals(myStr) because both the objects contain the exact same
     * String.
     */
    public static void main(String[] args) {
        String myStr = "good";
        char[] myCharArr = {'g', 'o', 'o', 'd'};
        String newStr = null;
        for (char ch : myCharArr) {
            newStr = newStr + ch;
        }
        System.out.println(null + "a");
        System.out.println(myStr + " " + newStr); //good nullgood
        System.out.println((newStr == myStr) + " " + (newStr.equals(myStr)));//false false
    }

}
