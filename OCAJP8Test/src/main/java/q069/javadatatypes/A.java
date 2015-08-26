/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q069.javadatatypes;

/**
 *
 * @author cstan
 */
public class A {

    static {
        System.out.println("A Loaded ");
    }

    public static void main(String[] args) {
        System.out.println(" A should have been loaded");
        A1 a1 = null;
        System.out.println(" A1 should not have been loaded");
        System.out.println(a1.i);
    }
}
