/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q407.inheritance;

/**
 *
 * What will the following program print when run?
 */
class A {

    A() {
        this("hello", " world");
    }

    A(String s) {
        System.out.println(s);
    }

    A(String s1, String s2) {
        this(s1 + s2);
    }
}

class B extends A {

    B() {
        super("good bye");
    }

    ;
    B(String s) {
        super(s, " world");
    }

    B(String s1, String s2) {
        this(s1 + s2 + " ! ");
    }
}

public class TestClass {

    /**
     * new B("good bye"); will call class B's one args constructor which in turn
     * calls super(s, " world "); (i.e. class A's two args constructor) which in
     * turn calls this(s1 + s2); (i.e. class A's one arg constructor with
     * parameter "good bye world") which prints it.
     */
    public static void main(String args[]) {
        A b = new B("good bye");
    }
}
