/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q391.inheritance;

/**
 *
 * Which of the following assignments are legal at compile time?
 */
interface MyIface {
};

class A {
};

class B extends A implements MyIface {
};

class C implements MyIface {
};

public class app {

    /**
     * The statements c = b and b = c are illegal, since neither of the classes
     * C and B is a subclass of the other. Even though a cast is provided, the
     * statement c = (C) b is illegal because the object referred to by b cannot
     * ever be of type C.
     */
    public static void main(String[] args) {
        A a = new A();
        B b = new B();
        C c = new C();

        b = c; // There is no relation between b and c.
        c = b; // There is no relation between b and c.
        MyIface i = c; // Because C implements I.
        c = (C) b; // Compiler can see that in no case can an object referred to by b can be of class c. So it is a compile time error.
        b = a; //It will fail at compile time because a is of class A and can potentially refer to an object of class A,
        // which cannot be assigned to b, which is a variable of class B.
        // To make it compile, you have to put an explicit cast,
        // which assures the compiler that a will point to an object of class B (or a subclass of B) at run time.
        // Note that, in this case, an explicit cast can take it through the compiler but it will then fail at run time
        // because a does not actually refer to an object of class B (or a subclass of B), so the JVM will throw a ClassCastException.

    }

}
