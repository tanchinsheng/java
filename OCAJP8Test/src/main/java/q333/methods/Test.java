/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q333.methods;

/**
 *
 * What will the following code print when compiled and run?
 */
public class Test {

    /**
     * The field a is static and there will be only one copy of a no matter how
     * many instances of Test you create. Changes made to it by one instance
     * will be reflected in the other instance as well. But field b is an
     * instance field. Each instance of Test will get its on copy of b.
     * Therefore, when you call p1.foo() and then p2.foo(), the same field a is
     * incremented 5 times twice and so it will print 10 10.
     */
    static int a = 0;
    int b = 5;

    public void foo() {
        while (b > 0) {
            b--;
            a++;
        }
    }

    public static void main(String[] args) {
        Test p1 = new Test();
        p1.foo();
        Test p2 = new Test();
        p2.foo();
        System.out.println(p1.a + " " + p2.a);
    }

}
