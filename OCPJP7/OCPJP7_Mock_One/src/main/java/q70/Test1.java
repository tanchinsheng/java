/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q70;

class Base1 {

    @Override
    protected void finalize() {
        System.out.println("In Base, finalize");
    }
}

class Derived1 extends Base {

    public Derived1() {
        super.finalize();
    }

    @Override
    protected void finalize() {
        System.out.println("In Derived, finalize");
    }
}

public class Test1 {

    public static void main(String[] args) {
        Derived1 d = new Derived1();
        d = null;
        Runtime.runFinalizersOnExit(true);
    }

}
