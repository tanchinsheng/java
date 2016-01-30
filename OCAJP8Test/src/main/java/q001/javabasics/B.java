package q001.javabasics;

/**
 *
 * The order of initialization of a class is: 1. All static constants, variables
 * and blocks.(Among themselves the order is the order in which they appear in
 * the code.) 2. All non static constants, variables and blocks.(Among
 * themselves the order is the order in which they appear in the code.) 3.
 * Constructor.
 */
class A {

    public A() {
    }

    public A(int i) {
        System.out.println(i);
    }
}

public class B {

    static A s1 = new A(1);
    A a = new A(2);

    public static void main(String[] args) {
        B b = new B();
        A a = new A(3);
    }
    static A s2 = new A(4);
}
