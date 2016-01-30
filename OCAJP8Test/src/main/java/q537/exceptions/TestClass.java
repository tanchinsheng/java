/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q537.exceptions;

/**
 *
 * @author cstan
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
    static void hello() throws MyException {
        int[] dear = new int[7];
        dear[0] = 747;
        foo();
    }

    static void foo() throws MyException {
        throw new MyException("Exception from foo");
    }

    public static void main(String[] args) {
        try {
            hello();
        } catch (MyException me) {
            System.out.println(me);
        }
    }

}
