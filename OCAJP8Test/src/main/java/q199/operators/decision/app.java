/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q199.operators.decision;

/**
 *
 * Which of the above variables will have the value 45?
 */
public class app {

    /**
     * Their values are 41 41 13 and 65. You may find similar questions in the
     * exam where you have to find the expression that returns the highest or
     * lower value. In such cases, you will need to evaluate each expression.
     */
    public static void main(String[] args) {
        int expr1 = 3 + 5 * 9 - 7;
        int expr2 = 3 + (5 * 9) - 7;
        int expr3 = 3 + 5 * (9 - 7);
        int expr4 = (3 + 5) * 9 - 7;
    }

}
