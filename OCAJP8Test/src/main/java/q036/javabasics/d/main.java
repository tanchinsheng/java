/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q036.javabasics.d;

/**
 *
 * class main's main method will be executed. final is a valid modifier for the
 * standard main method.  *
 * Note that final means a method cannot be overridden. Although static methods
 * can never be overridden. (they can be shadowed), making a static method final
 * prevents the subclass from implementing the same static method.
 */
class anothermain {

    public static void main(String[] args) {
        System.out.println("hello2");
    }
}

class main {

    public final static void main(String[] args) {
        System.out.println("hello");
    }
}
