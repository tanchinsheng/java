/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocpjp.pretest._08;

class Base {

    public static void foo(Base bObj) {
        // A static method is resolved statically.
        // Inside the static method, a virtual method is invoked, which is resolved dynamically.
        System.out.println("In _8Base.foo()");
        bObj.bar();
    }

    public void bar() {
        System.out.println("In _8Base.bar()");
    }
}
