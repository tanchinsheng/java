/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q529.exceptions;

/**
 *
 * What letters, and in what order, will be printed when the following program
 * is compiled and run?
 */
public class FinallyTest {

    /**
     * An exception is thrown in method m1() so println("A") will not be
     * executed. As there is no catch block, the exception will not be handled
     * and the main() method will throw the exception to the caller. Therefore,
     * println("C"); will also not be executed. 'finally' block is always
     * executed (even if there is a return in try but not if there is
     * System.exit() ) so println("B") is executed.
     */
    public static void m1() throws Exception {
        throw new Exception();
    }

    public static void main(String[] args) throws Exception {
        try {
            m1();
            System.out.println("A");
        } finally {
            System.out.println("B"); //It will print B and throw Exception.
        }
        System.out.println("C");
    }

}
