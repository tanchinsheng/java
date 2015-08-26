/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q081.javadatatypes;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * All the instance or static variables are given a default values if they
     * are not explicitly initialized. All numeric variable are given a value of
     * zero or equivalent to zero (i.e. 0 for integral types and 0.0 for
     * double/float). Booleans are initialized to false and objects are
     * initialized to null.
     *
     * Note that local (aka automatic) variables, such as the variables d and e
     * in the code given here, are not initialized automatically. They have to
     * be initialized explicitly. However, it is ok to leave them uninitialized
     * if you don't use them anywhere in the code (as is the case with the
     * variable d).
     */
    int a;
    int b = 0;
    static int c;

    public void m() {
        int d;
        int e = 0;
        // Line 1
        d++;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
