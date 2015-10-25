/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q453.inheritance;

/**
 *
 * Given the following two declarations, which of the options will compile?
 */
class A {

    public int getCode() {
        return 2;
    }
}

class AA extends A {

    public void doStuff() {
    }
}

public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        A a = null;
        AA aa = null;

        //a = (AA)aa; // ok
        //a = new AA(); //ok
        //aa = new A(); //nok
        //aa = (AA)a; //ok
        // a is declared as a reference of class A and therefore, at run time,
        // it is possible for a to point to an object of class AA (because A is a super class of AA).
        // Hence, the compiler will not complain. Although if a does not point to an object of class AA at run time,
        // a ClassCastException will be thrown.
        //aa = a;//nok:
        // A cast is required because the compiler needs to be assured that at run time a
        // will point to an object of class AA.
        ((AA) a).doStuff(); //ok
        // Once you cast a to AA, you can call methods defined in AA. Of course,
        // if a does not point to an object of class AA at runtime, a ClassCastException will be thrown.
        // In this particular case, a NullPointerException will be thrown because a points to null and a null
        // can be cast to any class.
    }

}