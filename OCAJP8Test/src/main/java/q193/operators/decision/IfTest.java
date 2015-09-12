/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q193.operators.decision;

/**
 *
 * What will be the result of attempting to compile and run the following class?
 */
public class IfTest {

    /**
     * Notice how the last "else" is associated with the last "if" and not the
     * first "if". Now, the first if condition returns true so the next 'if'
     * will be executed. In the second 'if' the condition returns false so the
     * else part will be evaluated which prints 'True True'.
     */
    public static void main(String args[]) {
        if (true) {
            if (false) {
                System.out.println("True False");
            } else {
                System.out.println("True True");
            }
        }
    }

}
