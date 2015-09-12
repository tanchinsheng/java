/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q307.constructors;

/**
 *
 * Given a class named Test, which of these would be valid definitions for a
 * constructor for the class?
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    Test(Test b) { //The constructor can take the same type as a parameter.
    }

    Test Test() { // A constructor cannot return anything.
    }

    private final Test() { //  A constructor cannot be final, static or abstract.
    }

    void Test() { // A constructor cannot return anything. Not even void.
    }

    public static void Test(String args[]) { //A constructor cannot be final, static or abstract.
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
