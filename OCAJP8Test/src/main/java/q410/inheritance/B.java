/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q410.inheritance;

/**
 *
 * Consider the following classes in one file named A.java...Which of the
 * following statements are correct...
 */
abstract class A {

    protected int m1() {
        return 0;
    }
}

public class B extends A {

    /**
     * The concept here is that an overriding method cannot make the overridden
     * method more private. The access hierarchy in increasing levels of
     * accessibility is: private->'no modifier'->protected->public ( public is
     * accessible to all and private is accessible to none except itself.) Here,
     * class B has no modifier for m1() so it is trying to reduce the
     * accessibility of protected to default. 'protected' means the method will
     * be accessible to all the classes in the same package and all the
     * subclasses (even if the subclass is in a different package). No modifier
     * (which is the default level) means the method will be accessible only to
     * all the classes in the same package. (i.e. not even to the subclass if
     * the subclass is in a different package.)
     */
    int m1() { // The overriding method cannot decrease the accessibility.
        return 1;
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
