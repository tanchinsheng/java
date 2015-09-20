/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q352.methods.overloading;

/**
 *
 * What will be the result of compiling and running the class?
 */
public class TestClass {

    /**
     * You cannot have two methods with the same signature (name and parameter
     * types) in one class. Also, even if you put one sayHello() method in other
     * class which is a subclass of this class, it won't compile because you
     * cannot override/hide a static method with a non static method and vice
     * versa.
     */
    public static void main(String[] args) {
        new TestClass().sayHello();
    }   //1

    public static void sayHello() {
        System.out.println("Static Hello World");
    }  //2

    public void sayHello() {
        System.out.println("Hello World ");
    }  //3 :It will say, method sayHello() is already defined.

}
