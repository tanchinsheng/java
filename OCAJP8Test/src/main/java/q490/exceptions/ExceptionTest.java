/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q490.exceptions;

/**
 *
 * What will the following code print when compiled and run? (Assume that
 * MySpecialException is an unchecked exception.)
 */
public class ExceptionTest {

    /**
     * @param args the command line arguments
     */
    static void doSomething() {
        int[] array = new int[4];
        array[4] = 4;
        doSomethingElse();

    }

    static void doSomethingElse() {
        throw new MySpecialException("Sorry, can't do something else");
    }

    public static void main(String[] args) {
        try {
            doSomething();
        } catch (MySpecialException e) {
            System.out.println(e);
        }
    }

}
