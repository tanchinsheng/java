/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q581.string;

/**
 *
 * Consider the following class...
 */
public class TestClass {

    /**
     * The method print()/println() of PrintStream takes an Object and prints
     * out a String that is returned by calling String.valueOf(object), which in
     * turn calls toString() on that object. Note that as toString() is defined
     * in Object class, all objects in java have this method. So it prints 2
     * first. The second object's toString() returns null, so it prints "null".
     * There is no NullPointerException because no method is called on null.
     * Now, the other feature of print/println methods is that if they get null
     * as input parameter, they print "null". They do not try to call toString()
     * on null. So, if you have, Object o = null; System.out.println(o); will
     * print null and will not throw a NullPointerException.
     */
    int i;

    public TestClass(int i) {
        this.i = i;
    }

    public String toString() {
        if (i == 0) {
            return null;
        } else {
            return "" + i;
        }
    }

    public static void main(String[] args) {
        TestClass t1 = new TestClass(0);
        TestClass t2 = new TestClass(2);
        System.out.println(t2); // 2
        System.out.println("" + t1); //null
    }

}
