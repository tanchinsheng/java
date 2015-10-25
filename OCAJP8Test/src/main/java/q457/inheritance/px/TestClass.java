/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q457.inheritance.px;

/**
 *
 * Identify the correct statement(s).
 */
import q457.inheritance.p1.Movable;
import q457.inheritance.p2.Donkey;

public class TestClass {

    /**
     * There is no problem with the code. All variables in an interface are
     * implicitly static and final. All methods in an interface are public.
     * There is no need to define them so explicitly. Therefore, the location
     * variable in Movable is static and the move() method is public. Now, when
     * you call m.move(10) and m.moveBank(20), the instance member location of
     * Donkey is updated to 190 because  the reference m refers to a Donkey at
     * run time and so move and moveBack methods of Donkey are invoked at
     * runtime. However, when you print m.location, it is the Movable's location
     * (which is never updated) that is printed.
     */
    public static void main(String[] args) {
        Movable m = new Donkey();
        m.move(10);
        m.moveBack(20);
        System.out.println(m.location);
    }

}
