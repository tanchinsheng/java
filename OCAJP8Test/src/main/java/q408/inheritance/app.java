/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q408.inheritance;

/**
 *
 * Which of the following calls are virtual calls?
 */
class A {

    public void mA() {
    }
;

}

class B extends A {

    public void mA() {
    }

    public void mB() {
    }
}

class C extends B {

    public void mC() {
    }
}

public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        A x = new B();
        x.mA();
        x.mB();

        B y = new B();
        y.mA();

        B z = new C();
        z.mC();
        z.mB();
    }

}
