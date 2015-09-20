/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q363.methods.overloading;

/**
 *
 * @author cstan
 */
class XXX {

    public void m() {
        throw new RuntimeException();
    }
}

class YYY extends XXX {

    public void m() throws Exception {
        throw new Exception();
    }
}

public class TestClass {

    /**
     * Remember that an overriding method can only throw a subset of checked
     * exceptions declared in the throws clause of the overridden method. Here,
     * method m in XXX does not declare any checked exception in its throws
     * clause, therefore, method m in YYY cannot declare any checked exception
     * in its throws clause either. Class YYY will, therefore, not compile.
     */
    public static void main(String[] args) {
        ______ obj = new ______();
        obj.m();
    }

}
