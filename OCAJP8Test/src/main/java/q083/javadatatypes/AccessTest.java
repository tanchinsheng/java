/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q083.javadatatypes;

/**
 *
 * @author cstan
 */
public class AccessTest {

    /**
     * @param args the command line arguments
     */
    String a = "x";
    static char b = 'x';
    String c = "x";

    class Inner {

        String a = "y";

        String get() {
            String c = "temp";
            // Which statements can be inserted at line 1 in the following code
            // to make the program write x on the standard output when run?
            // Line 1
            // It will reassign 'temp' to c!
            // c = c; // print temp
            // It will assign "y" to c.
            // c = this.a; // print y
            // Because b is static.
            // c = "" + AccessTest.b; // print x
            // c = AccessTest.this.a;// print x
            c = "" + b;// print x
            return c;
        }
    }

    AccessTest() {
        System.out.println(new Inner().get());
    }

    public static void main(String args[]) {
        new AccessTest();
    }

}
