/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q489.exceptions;

/**
 *
 * What will be the output of the following class.
 */
public class Test {

    /**
     * If evaluation of the left-hand operand of a binary operator completes
     * abruptly, no part of the right-hand operand appears to have been
     * evaluated. So, as doIt() throws exception, j = 2 never gets executed.
     */
    public static int doIt() throws Exception {
        throw new Exception("FORGET IT");
    }

    public static void main(String[] args) {
        int j = 1;
        try {
            int i = doIt() / (j = 2);
        } catch (Exception e) {
            System.out.println(" j = " + j);
        }
    }

}
