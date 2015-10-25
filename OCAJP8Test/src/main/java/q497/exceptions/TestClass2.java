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
public class TestClass2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        A a = new B();
        a.m(); //A's m() declares 'throws SomeException', which is a checked exception,
        // while the main() method doesn't. So a.m() must be wrapped in a try/catch block.
    }

}
