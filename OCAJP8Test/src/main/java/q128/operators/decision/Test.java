/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q128.operators.decision;

/**
 *
 * @author cstan
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args[0].equals("open")) {
            if (args[1].equals("someone")) {
                System.out.println("Hello!");
            } else {
                System.out.println("Go away " + args[1]);
            }
        }
    }

}
