/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q187.operators.decision;

/**
 *
 * The following program will print java.lang.ArithmeticException: / by zero
 */
public class Test {

    /**
     * It will print Forget It because before the division can take place doIt()
     * will throw an exception.
     */
    public static void main(String[] args) {
        int d = 0;
        try {
            int i = 1 / (d * doIt());
        } catch (Exception e) {
            System.out.println(e);
            // java.lang.Exception: Forget It
        }
    }

    public static int doIt() throws Exception {
        throw new Exception("Forget It");
    }

}
