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
public class Q05 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String s1 = "java";
        StringBuilder s2 = new StringBuilder("java");
        if (s1 == s2) {
            System.out.print("1");
        }
        if (s1.equals(s2)) {
            System.out.print("2");
        }
    }

}
