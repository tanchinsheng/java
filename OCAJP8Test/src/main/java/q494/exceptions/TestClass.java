/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q494.exceptions;

/**
 *
 * @author cstan
 */
class MyException extends Exception {
}

public class TestClass {

    /**
     * @param args the command line arguments
     */
    public void m1() throws MyException {
        throw new MyException();
    }

    public void m2() throws RuntimeException {
        throw new NullPointerException();
    }

    public static void main(String[] args) {
        TestClass tc = new TestClass();
        try {
            tc.m1();
        } catch (MyException e) {
            tc.m1();
        } finally {
            tc.m2();
        }
    }

}
