/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q505.exceptions;

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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            m2();
        } finally {
            m3();
        }
        catch (NewException e){}
        //Because a catch block cannot follow a finally block!
    }

    public static void m2() throws NewException {
        throw new NewException();
    }

    public static void m3() throws AnotherException {
        throw new AnotherException();
    }
}
