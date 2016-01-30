/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q515.exceptions;

/**
 *
 * What is wrong with the following code written in a single file named
 * TestClass.java?
 */
class SomeThrowable extends Throwable {
}

class MyThrowable extends SomeThrowable {
}

public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void m1() throws MyThrowable {
        throw new MyThrowable();
    }

    public static void main(String[] args) throws SomeThrowable {
        try {
            m1();
        } catch (SomeThrowable e) {
            throw e;
        } finally {
            System.out.println("Done");
        }
    }

}
