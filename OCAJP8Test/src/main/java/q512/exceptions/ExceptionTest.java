/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q512.exceptions;

/**
 *
 * What will be the result of compiling and running the following program ?
 */
class NewException extends Exception {
}

class AnotherException extends Exception {
}

public class ExceptionTest {

    /**
     * It will compile but will throw AnotherException when run. m2() throws
     * NewException, which is not caught anywhere. But before exiting out of the
     * main method, finally must be executed. Since finally throw
     * AnotherException (due to a call to m3() ), the NewException thrown in the
     * try block ( due to call to m2() ) is ignored and AnotherException is
     * thrown from the main method.
     */
    public static void m2() throws NewException {
        throw new NewException();
    }

    public static void m3() throws AnotherException {
        throw new AnotherException();
    }

    public static void main(String[] args) throws Exception {
        try {
            m2();
        } finally {
            m3();
        }
    }

}
