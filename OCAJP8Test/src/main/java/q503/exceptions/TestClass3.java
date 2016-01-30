/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q503.exceptions;

/**
 *
 * @author cstan
 */
public class TestClass3 {

    /**
     * What can replace XXXX?
     */
    //public void myMethod() throws XXXX {
    public void myMethod() {
        throw new MyException();
        //It is needed because MyException is a checked exception.
        //Any exception that extends java.lang.Exception but is not a subclass of java.lang.RuntimeException is a checked exception.

    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
