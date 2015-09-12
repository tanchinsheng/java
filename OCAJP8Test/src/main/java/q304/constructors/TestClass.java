/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q304.constructors;

/**
 *
 * Which of the following can be used as a constructor for the class given
 * below?
 */
public class TestClass {

    public void TestClass() {
    }

    public TestClass() {
    }

    public static TestClass() {
    }

    public final TestClass() {
    }

    public TestClass(int x) {
    }

    /**
     * You can use only one of public protected and private. Unlike methods, a
     * constructor cannot be abstract, static, final, native, or synchronized. A
     * constructor is not inherited, so there is no need to declare it final and
     * an abstract constructor could never be implemented. A constructor is
     * always invoked with respect to an object, so it makes no sense for a
     * constructor to be static. There is no practical need for a constructor to
     * be synchronized, because it would lock the object under construction,
     * which is normally not made available to other threads until all
     * constructors for the object have completed their work. The lack of native
     * constructors is an arbitrary language design choice that makes it easy
     * for an implementation of the Java Virtual Machine to verify that
     * superclass constructors are always properly invoked during object
     * creation.
     */
    public static void main(String[] args) {
    }

}
