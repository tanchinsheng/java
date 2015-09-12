/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q132.operators.decision;

/**
 *
 * Given the following LOCs:
 */
public class app {

    /**
     * Note that none of the terms in the expression 1 - rate/100*1 - rate/100;
     * is double or float. They are all ints. So the result of the expression
     * will be an int. Since an int can be assigned to a variable of type int,
     * long, float or double, amount can be int, long, float or double.
     */
    public static void main(String[] args) {

        int rate = 10;
        // XXX amount = 1 - rate / 100 * 1 - rate / 100;
        int amount1 = 1 - rate / 100 * 1 - rate / 100;
        long amount2 = 1 - rate / 100 * 1 - rate / 100;
        float amount3 = 1 - rate / 100 * 1 - rate / 100;
        double amount4 = 1 - rate / 100 * 1 - rate / 100;
    }

}
