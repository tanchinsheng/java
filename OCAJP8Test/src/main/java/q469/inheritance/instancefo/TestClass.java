/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q469.inheritance.instancefo;

/**
 *
 * @author cstan
 */
class A {
}

class B extends A {
}

class C extends B {
}

class D extends C {
}

public class TestClass {

    /**
     * The program will print A, B and C when run. The object denoted by
     * reference a is of type C. The object is also an instance of A and B,
     * since C is a subclass of B and B is a subclass of A. The object is not an
     * instance of D.
     */
    public static void main(String[] args) {

        B b = new C();
        A a = b;
        if (a instanceof A) {
            System.out.println("A");
        }
        if (a instanceof B) {
            System.out.println("B");
        }
        if (a instanceof C) {
            System.out.println("C");
        }
        if (a instanceof D) {
            System.out.println("D");
        }
    }

}
