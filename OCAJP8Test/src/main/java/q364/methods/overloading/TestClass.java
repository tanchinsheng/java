/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q364.methods.overloading;

/**
 *
 * What will be the output?
 */
class OverloadingTest {

    void m1(int x) {
        System.out.println("m1 int");
    }

    void m1(double x) {
        System.out.println("m1 double");
    }

    void m1(String x) {
        System.out.println("m1 String");
    }

}

public class TestClass {

    /**
     * Here, m1() is overloading for three different argument types. So when you
     * call ot.m1(1.0), the one with argument of type double will be invoked.
     */
    public static void main(String[] args) {
        OverloadingTest ot = new OverloadingTest();
        ot.m1(1.0);
    }

}
