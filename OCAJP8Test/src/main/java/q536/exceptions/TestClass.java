/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q536.exceptions;

/**
 *
 * What will be the output when the following program is run?
 */
class MyException extends Exception {

    public MyException(String msg) {
        super(msg);
    }
}

public class TestClass {

    /**
     * @param args the command line arguments
     */
    static void doTest() throws MyException {
        int[] array = new int[10];
        array[10] = 1000;
        doAnotherTest();
    }

    static void doAnotherTest() throws MyException {
        throw new MyException("Exception from doAnotherTest");
    }

    public static void main(String[] args) {
        try {
            doTest();
        } catch (MyException me) {
            System.out.println(me);
        }
    }

}
