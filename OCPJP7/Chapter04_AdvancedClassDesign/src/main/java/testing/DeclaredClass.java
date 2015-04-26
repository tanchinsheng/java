/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;

import java.io.Serializable;

abstract class AbstractClass implements Serializable {

    static int i;

    static final int j = 0; // final variable must be initialised

    static int getI() {
        return i;
    }

}

public class DeclaredClass extends AbstractClass {

    final int a;
    static final Object o = 1;

    public DeclaredClass() {
        a = 0;
        // o = 2;
    }

    public static void main(String[] args) {
        AbstractClass c = new DeclaredClass();
        AbstractClass a = new AbstractClass() {
        };
    }

}
