/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q337.methods;

/**
 *
 * What will the following code print when run?
 */
class A {

    String value = "test";

    A(String val) {
        this.value = val;
    }
}

public class TestClass {

    /**
     * There is no method named print() defined in class A. Further, there is no
     * such method in class Object either. To print the contents of an object
     * you can use toString() method that returns a String:
     * System.out.println(a.toString()); However, for this to print a meaningful
     * value, class A should override the Object class's toString() method to
     * return a meaningful String.
     */
    public static void main(String[] args) {
        new A("new test").print();
    }

}
