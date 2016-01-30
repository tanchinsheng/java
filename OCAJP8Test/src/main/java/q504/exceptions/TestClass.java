/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q504.exceptions;

/**
 *
 * Which digits and in what order will be printed when the following program is
 * run?
 */
public class TestClass {

    /**
     * Division by 0 throws a java.lang.ArithmeticException, which is a
     * RuntimeException. This is caught by the first catch clause because it is
     * the first block that can handle ArithmeticException. This prints 1. Now,
     * as the exception is already handled, control goes to finally which prints
     * 4 and then the try/catch/finally ends and 5 is printed. Remember :
     * finally is always executed even if try or catch return; (Except when
     * there is System.exit() in try or catch.)
     */
    public static void main(String[] args) {
        int k = 0;
        try {
            int i = 5 / k;
        } catch (ArithmeticException e) {
            System.out.println("1");
        } catch (RuntimeException e) {
            System.out.println("2");
            return;
        } catch (Exception e) {
            System.out.println("3");
        } finally {
            System.out.println("4");
        }
        System.out.println("5");
    }

}
