/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q152.operators.decision;

/**
 *
 * @author cstan
 */
public class Test {

    /**
     * Java parses the expression from left to right. Once it realizes that the
     * left operand of a conditional "or" operator has evaluated to true, it
     * does not even try to evaluate the right side expression.
     */
    static boolean a;
    static boolean b;
    static boolean c;

    public static void main(String[] args) {
        boolean bool = (a = true) || (b = true) && (c = true);
        System.out.print(a + ", " + b + ", " + c);
    }
}
