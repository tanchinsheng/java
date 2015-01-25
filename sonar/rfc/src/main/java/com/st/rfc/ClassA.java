package com.st.rfc;

/**
 *
 * @author cstan
 */

public class ClassA {

    // call (constructor of class B) => +1
    private ClassB classB = new ClassB();  

    public void doSomething() {       // method declaration => +1
        System.out.println("doSomething");   // call (System.out.println) => +1
    }

    public void doSomethingBasedOnClassB() {  // method declaration => +1
        // call (System.out.println) => 0 because already counted on line 5 + call (toString) => +1
        System.out.println(classB.toString()); 
    }
}