/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q454.inheritance;

/**
 *
 * Which is the first line that will cause compilation to fail in the following
 * program?
 */
class B extends A {
}

class A {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        A a = new A();
        B b = new B();
        a = b;  // 1
        b = a;  // 2 : Because 'a' is declared of class A and 'b' is of B which is a subclass of A. So an explicit cast is needed.
        a = (B) b; // 3
        b = (B) a; // 4
    }

}
