/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q25;

class SuperClass1 {

    SuperClass1() {
        foo();
    }

    private void foo() {
        System.out.println("In private, SuperClass.foo()");
    }
}

public class SubClass1 extends SuperClass1 {

    private String member;

    public SubClass1() {
        member = "HI";
    }

    public void foo() {
        System.out.println("In Derived.foo(): " + member.toLowerCase());
    }

    public static void main(String[] args) {
        SuperClass1 reference = new SubClass1();
        //reference.foo();
    }

}
