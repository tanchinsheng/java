/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q383.inheritance;

/**
 *
 * Which statement regarding the following code is correct?
 */
class A {

    public int i = 10;
    private int j = 20;
}

class B extends A {

    private int i = 30; //1    
    public int k = 40;
}

class C extends B {
}

public class TestClass {

    /**
     * You cannot access c.i because i is private in B. But you can access (
     * (A)c).i because i is public in A. Remember that member variables are
     * hidden and not overridden. So, B's i hides A's i and since B's i is
     * private, you can't access A's i unless you cast the reference to A. You
     * cannot access c.j because j is private in A.
     */
    public static void main(String[] args) {

        C c = new C();
        System.out.println(c.i); //2       
        System.out.println(c.j); //3       
        System.out.println(c.k);
        System.out.println(((A) c).i);
    }

}
