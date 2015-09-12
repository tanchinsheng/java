/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q145.operators.decision;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * Object class's equals() method just checks whether the two references are
     * pointing to the same location or not. In this case they really are
     * pointing to the same location because of obj2 = obj1; so it returns true.
     */
    public static void main(String[] args) {
        Object obj1 = new Object();
        Object obj2 = obj1;
        if (obj1.equals(obj2)) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }

        if (obj1 == obj2) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }

    }

}
