/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q467.inheritance.instancefo;

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

public class app {

    /**
     * D extends C, which extends B, which extends A. This means, D is-a C, C
     * is-a B, and B is-a A. Therefore, D is-a A. Hence, d instanceof A will
     * return true.
     */
    public static void main(String[] args) {
        D d = new D();
        if (d instanceof A) {
            System.out.println("true");
        }
    }

}
