/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q060.javadatatypes;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * This question tests your knowledge on the default values of uninitialized
     * primitives and object references. booleans are initialized to false,
     * numeric types to 0 and object references to null. Elements of arrays are
     * initialized to the default values of their types. So, elements of a
     * boolean array are initialized to false. int, char, float to 0 and Objects
     * to null. In this case, b is false. So the else part of if(b) is executed.
     * ch is a numeric type to its value is 0. ba[0] = false assigns false to
     * ba[0] and returns false which is assigned to x. Finally, x and ba[0] are
     * printed as false false.
     */
    static boolean b;
    static int[] ia = new int[1];
    static char ch;
    static boolean[] ba = new boolean[1];

    public static void main(String[] args) {
        boolean x = false;
        if (b) {
            x = (ch == ia[ch]);
        } else {
            x = (ba[ch] = b);
        }
        System.out.println(x + " " + ba[ch]);
    }
}
