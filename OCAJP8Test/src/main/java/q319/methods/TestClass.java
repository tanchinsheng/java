/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q319.methods;

/**
 *
 * What will be the output when the following program is run?
 */
public class TestClass {

    /**
     * Note that Arrays are Objects (i.e. cA instanceof Object is true) so are
     * effectively passed by reference. So in m1() the change in cA[1] done by
     * m2() is reflected everywhere the array is used. c is a primitive type and
     * is passed by value. In method m2() the passed parameter c is different
     * than instance variable 'c' as local variable hides the instance variable.
     * So instance member 'c' keeps its default (i.e. 0) value.
     */
    char c; // c is an instance variable of numeric type so it will be given a default value of 0, which prints as empty space.

    public void m1() {
        char[] cA = {'a', 'b'};
        m2(c, cA);
        System.out.println(((int) c) + "," + cA[1]);
        // 0,m : Because of the explicit cast to int in the println() call, c will be printed as 0.
    }

    public void m2(char c, char[] cA) {
        c = 'b';
        cA[1] = cA[0] = 'm';
    }

    public static void main(String[] args) {
        new TestClass().m1();
    }

}
