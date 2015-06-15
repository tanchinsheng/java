/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter3;

/**
 *
 * @author cstan
 */
public class Q02 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String s = "Hello";
        String t = new String(s);
        if ("Hello".equals(s)) {
            System.out.println("one");
        }
        if (t == s) {
            System.out.println("two");
        }
        if (t.equals(s)) {
            System.out.println("three");
        }
        if ("Hello" == s) {
            System.out.println("four");
        }
        if ("Hello" == t) {
            System.out.println("five");
        }
    }

}
