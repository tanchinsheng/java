/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q351.methods;

/**
 *
 * Which of the statements regarding the following code are correct?
 */
public class TestClass {

    /**
     * The code will compile and run without any problem. All the instance or
     * static variables are given a default values if not explicitly
     * initialized. All numeric variable are given a value of zero or equivalent
     * to zero (i.e. 0.0 for double or float ). booleans are initialized to
     * false and objects references are initialized to null.
     */
    static int a;
    int b;

    public TestClass() {
        int c;
        c = a;
        a++;
        b += c;
    }

    public static void main(String args[]) {
        new TestClass();
    }

}
