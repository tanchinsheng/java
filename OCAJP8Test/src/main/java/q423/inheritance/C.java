/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q423.inheritance;

/**
 *
 * How can you let perform_work() method of A to be called from an instance
 * method in C?
 */
class A {

    public void perform_work() {
        System.out.println("A");
    }
}

class B extends A {

    public void perform_work() {
        System.out.println("B");
    }
}

public class C extends B {

    /**
     * The method in C needs to call a method in a superclass two levels up. But
     * super is a keyword and not an attribute so super.super.perform_work( )
     * strategy will not work. There is no way to go more than one level up for
     * methods. Remember that this problem doesn't occur for instance variables
     * because variable are never overridden. They are shadowed. So to access
     * any of the super class's variable, you can unshadow it using a cast. For
     * example, ((A) c).data; This will give you the data variable defined in A
     * even if it is shadowed in B as well as in C.
     */
    public void perform_work() {
        System.out.println("C");
    }

    public static void main(String[] args) {
        C c = new C();
    }

}
