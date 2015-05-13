/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q25;

class SuperClass {

    SuperClass() {
        foo();
    }

    public void foo() {
        System.out.println("In SuperClass.foo()");
    }
}

public class SubClass extends SuperClass {

    private String member;

    public SubClass() {
        member = "HI";
    }

    @Override
    public void foo() {
        System.out.println("In Derived.foo(): " + member.toLowerCase());
        //System.out.println("In Derived.foo()");
    }

    public static void main(String[] args) {
        SuperClass reference = new SubClass();
        reference.foo();
    }

}
