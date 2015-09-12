/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q167.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(5.5 % 3); // It can be used on floating points operands also. For example, 5.5 % 3 = 2.5
        System.out.println(true & false); // unlike &&, & will not "short circuit" the expression if used on boolean parameters.
        System.out.println(true && false); // !, && and || operate only on booleans.
        System.out.println(2 && 1);
        System.out.println(~1); // ~ Operates only on integral types
    }

}
