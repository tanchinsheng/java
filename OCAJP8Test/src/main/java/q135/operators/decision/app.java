/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q135.operators.decision;

/**
 *
 * What is the result of executing the following fragment of code:
 */
public class app {

    /**
     * The expression b1 = i1 == i2 will be evaluated as b1 = (i1 == i2) because
     * == has higher precedence than =. Further, all an if statement needs is a
     * boolean. Now i1 == i2 returns false which is a boolean and since b1 =
     * false is an expression and every expression has a return value (which is
     * actually the Left Hand Side of the expression), it returns false which is
     * again a boolean. Therefore, in this case, the else condition will be
     * executed.
     */
    public static void main(String[] args) {
        boolean b1 = false;
        int i1 = 2;
        int i2 = 3;
        if (b1 = i1 == i2) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}
