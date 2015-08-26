/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q075.javadatatypes;

/**
 *
 * static and final are valid modifiers for both member field and method
 * declarations within a class.
 *
 * transient and volatile modifiers are only valid for member field
 * declarations.
 *
 * abstract and native are only valid for member methods. Note: a class
 * declaration can have only final, abstract and public as modifiers, unless it
 * is a nested class, in which case, it can be declared private or  protected as
 * well. Within a method, a local variable may be declared as final.
 */
// final, abstract and public are only valid for class
// private and protected are allowed in nested class
public class app {

    // transient  and volalile modifiers are only valid for member field
    // static and final are valid modifiers for both member field and method
    static int sa;
    final Object[] objArr = {null}; //Declares and defines an array of Objects of length 1.
    abstract int t; // Variables can't be declared as abstract or native.
    native int u;

    abstract void format();

    native void test();

    final static private double PI = 3.14159265358979323846;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
