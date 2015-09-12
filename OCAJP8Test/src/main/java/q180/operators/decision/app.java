/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q180.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * All that if() needs is a boolean, now b1 == false returns true, which is
     * a boolean and since b2 = true is an expression and every expression has a
     * return value (which is the Left Hand Side of the expression), it returns
     * true, which is again a boolean. FYI: the return value of expression  i =
     * 10;  is 10 (an int).
     */
    public static void main(String[] args) {
        boolean b1 = false;
        boolean b2 = false;
        if (b2 = b1 == false) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }

}
