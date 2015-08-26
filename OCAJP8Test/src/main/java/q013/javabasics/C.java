/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q013.javabasics;

/**
 *
 * Access modifiers (public/private/protected) are valid only inside the scope
 * of a class, not of a method.
 */
public class C {

    int a;

    public void m1() {
    private int b = 0;
    a  = b;
}
}
