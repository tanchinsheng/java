/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q497.exceptions;

/**
 *
 * @author cstan
 */
public class TestClass3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        A a = new B();
        ((B) a).m();
        // Due the explicit casting of 'a' to B, the compiler knows that 'a' will point to an object of class B
        // (or its subclass), whose method m() does not throw an exception. So there is no need for a try catch block here.
    }

}
