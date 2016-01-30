/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q531.exceptions;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void doStuff() throws Exception {
        System.out.println("Doing stuff...");
        if (Math.random() > 0.4) {
            throw new Exception("Too high!");
        }
        System.out.println("Done stuff.");
    }

    public static void main(String[] args) throws Exception {
        doStuff();
        System.out.println("Over");
    }

}
