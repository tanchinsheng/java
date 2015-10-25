/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q499.exceptions;

/**
 *
 * What will be the result of attempting to compile and run the following
 * program?
 */
public class TestClass {

    /**
     * You are throwing an exception and there is no try or catch block, or a
     * throws clause. So it will not compile. If you either put a try catch
     * block or declare a throws clause for the method then it will throw a
     * NullPointerException at run time because e is null. A method that throws
     * a 'checked' exception i.e. an exception that is not a subclass of Error
     * or RuntimeException, either must declare it in throws clause or put the
     * code that throws the exception in a try/catch block.
     */
    public static void main(String[] args) {

        Exception e = null;
        throw e;
    }

}
