/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q426.inheritance.b;

import q426.inheritance.a.*;

/**
 *
 * Note that there is no modifier for A's constructor. So it has default access.
 * This means only classes in package a can use it. Also note that class B is in
 * a different package and is extending from A. In B's constructor the compiler
 * will automatically add super() as the first line. But since A() is not
 * accessible in B, this code will not compile.
 */
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
