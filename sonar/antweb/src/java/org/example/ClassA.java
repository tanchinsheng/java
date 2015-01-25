/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example;

/**
 *
 * @author cstan
 */
public class ClassA {

    private ClassB classB = new ClassB();

    public void doSomething() {
        System.out.println("doSomething");
    }

    public void doSomethingBasedOnClassB() {
        System.out.println(classB.toString());
    }
}
