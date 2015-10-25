/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q463.inheritance;

/**
 *
 * Which of the following lines of code that, when inserted at line 1, will make
 * the overriding method in SubClass invoke the overridden method in BaseClass
 * on the current object with the same parameter.
 */
class BaseClass {

    public void print(String s) {
        System.out.println("BaseClass :" + s);
    }
}

public class SubClass extends BaseClass {

    /**
     * This is the right syntax to call the base class's overridden method.
     * However, note that there is no way to call a method if it has been
     * overriden more than once. For example, if you make BaseClass extend from
     * another base class SubBase, and if SubBase also has the same method, then
     * there is no way to invoke SubBase's print method from SubClass's print
     * method. You cannot have something like super.super.print(s);
     */
    public void print(String s) {
        System.out.println("SubClass :" + s);
        // Line 1
        super.print(s);
    }

    public static void main(String args[]) {
        SubClass sc = new SubClass();
        sc.print("location");
    }

}
