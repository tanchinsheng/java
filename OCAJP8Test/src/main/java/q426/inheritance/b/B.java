package q426.inheritance.b;

import q426.inheritance.a.*;

public class B extends A {

    B() { // Because A() is not accessible in B.
    }

    public void print() {
        System.out.println("B");
    }

    public static void main(String[] args) {
        new B();
    }
}
