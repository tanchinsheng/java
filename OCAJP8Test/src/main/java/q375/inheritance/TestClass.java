/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q375.inheritance;

/**
 *
 * Which one of these is a proper definition of a class TestClass that cannot be
 * sub-classed?
 */
final class TestClass {

    /**
     * A final class cannot be subclassed. Although declaring a method static
     * usually implies that it is also final, this is not true for classes. An
     * inner class can be declared static and still be extended. Note that for
     * classes, final means it cannot be extended, while for methods, final
     * means it cannot be overridden in a subclass. The native keyword can only
     * be used on methods, not on classes and or variables.
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
