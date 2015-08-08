/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String s = "Hello";
        if (s instanceof String) {
            System.out.println("s is a String");
        }

        String s1 = "Hello";
        String s2 = "Hello";
        if (s1 == s2) { // check  same object
            System.out.println("Match");
        } else {
            System.out.println("No Match!");
        }

        String s3 = "Hello";
        String s4 = "Hello";
        if (s3.equals(s4)) { // check  same object
            System.out.println("Match");
        } else {
            System.out.println("No Match!");
        }

        String s5 = new String("Hello");
        String s6 = new String("Hello");
        if (s5 == s6) {
            System.out.println("Match");
        } else {
            System.out.println("No Match!");
        }

        String s7 = new String("Hello");
        String s8 = "Hello";
        if (s7 == s8) {
            System.out.println("Match");
        } else {
            System.out.println("No Match!");
        }

//s is a String
//Match
//Match
//No Match!
//No Match!
    }

}
