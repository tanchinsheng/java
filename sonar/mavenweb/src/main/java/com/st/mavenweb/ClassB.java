/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.st.mavenweb;

/**
 *
 * @author cstan
 */
public class ClassB {

    private ClassA classA = new ClassA();

    public void doSomethingBasedOneClassA() {
        System.out.println(classA.toString());
    }

    public String toString() {
        return "classB";
    }
}