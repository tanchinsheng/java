/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q372.inheritance;

/**
 *
 * What will be the result of attempting to compile and run class B?
 */
class A {

    final int fi = 10;
}

public class B extends A {

    /**
     * Note that a final variable can be shadowed. Here, although fi in A is
     * final, it is shadowed by fi of B. So b.fi = 20; is valid since B's fi is
     * not final.
     */
    int fi = 15;

    public static void main(String[] args) {
        B b = new B();
        b.fi = 20;
        System.out.println(b.fi);
        System.out.println(((A) b).fi);
    }

}
