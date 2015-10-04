/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q401.inheritance;

/**
 *
 * What will be the result of attempting to compile and run the following
 * program?
 */
class A {

    int i = 10;

    int m1() {
        return i;
    }
}

class B extends A {

    int i = 20;

    int m1() {
        return i;
    }
}

class C extends B {

    int i = 30;

    int m1() {
        return i;
    }
}

public class TestClass {

    /**
     * Remember : variables are SHADOWED and methods are OVERRIDDEN. Which
     * variable will be used depends on the class that the variable is declared
     * of. Which method will be used depends on the actual class of the object
     * that is referenced by the variable. So, in line o1.m1(), the actual class
     * of the object is C, so C's m1() will be used. So it returns 30. In line
     * o2.i, o2 is declared to be of class B, so B's i is used. So it returns
     * 20.
     */
    public static void main(String[] args) {
        A o1 = new C();
        B o2 = (B) o1;
        System.out.println(o1.m1()); // 30
        System.out.println(o2.i); // 20
    }

}
