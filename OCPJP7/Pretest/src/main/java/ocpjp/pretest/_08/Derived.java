/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ocpjp.pretest._08;

/**
 *
 * @author cstan
 */
public class Derived extends Base {

    public static void foo(Base bObj) {
        System.out.println("In _8Derived.foo()");
        bObj.bar();
    }

    public void bar() {
        System.out.println("In _8Derived.bar()");
    }
}
