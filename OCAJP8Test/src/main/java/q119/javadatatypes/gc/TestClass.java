/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q119.javadatatypes.gc;

/**
 *
 * In the following code, after which statement (earliest), the object
 * originally held in s, may be garbage collected ?
 */
public class TestClass {

    /**
     * In this case, since there is only one reference to Student object, as
     * soon as it is set to null, the object held by the reference is eligible
     * for GC, here it is done at line 6. Note that although an object is
     * created at line 7 with same parameters, it is a different object and it
     * will be eligible for GC after line 10.
     */
    public static void main(String args[]) {
        Student s = new Student("Vaishali", "930012");
        s.grade();
        System.out.println(s.getName());

        s = null;
        s = new Student("Vaishali", "930012");
        s.grade();
        System.out.println(s.getName());
        s = null;
    }

}
