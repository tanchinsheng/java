/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q357.methods.overloading;

/**
 *
 * What will be printed?
 */
public class TestClass {

    /**
     * Here, we have three overloaded probe methods but there is no probe method
     * that takes a String parameter. The only one that is able to accept a
     * String is the one that takes Object as a parameter. So that method will
     * be called.
     *
     * A String cannot be assigned to a variable of class Integer or Long
     * variable, but it can be assigned to a variable of class Object.
     */
    void probe(Integer x) {
        System.out.println("In Integer");
    } //2

    void probe(Object x) {
        System.out.println("In Object");
    } //3

    void probe(Long x) {
        System.out.println("In Long");
    } //4

    public static void main(String[] args) {
        String a = "hello";
        new TestClass().probe(a);
    }

}
