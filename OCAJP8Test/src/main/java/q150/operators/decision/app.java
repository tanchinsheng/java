/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q150.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * All an if(...) needs is a boolean. x = 3 is not valid because the return
     * value of this expression is 3 which is not a boolean.
     */
    public static void main(String[] args) {
        if (8 == 81) {
            System.out.println("true1");
        }
        if (true) {
            System.out.println("true2");
        }

        boolean bool;
        if (bool = false) {
            System.out.println("true3");
        }

        int x = 0;
        if (x == 10 ? true : false) {
            System.out.println("true4");
        } // assume that x is an int

        int y;
        if (y = 3) {
            System.out.println("true5");
        } // assume that x is an int

    }

}
