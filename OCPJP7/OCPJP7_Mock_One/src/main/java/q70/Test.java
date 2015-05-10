/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q70;

class Base {

    @Override
    protected void finalize() {
        System.out.println("In Base, finalize");
    }
}

class Derived extends Base {

    @Override
    protected void finalize() {
        System.out.println("In Derived, finalize");
    }
}

public class Test {
    
    public static void main(String[] args) {
        Derived d = new Derived();
        d = null;
        Runtime.runFinalizersOnExit(true);
    }

}
