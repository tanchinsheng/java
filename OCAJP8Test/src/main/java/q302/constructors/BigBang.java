/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q302.constructors;

/**
 *
 * Given the following source code, which of the lines that are commented out
 * may be reinserted without introducing errors?
 */
abstract class Bang {

    //abstract void f();  //(0): If this line is inserted, then class BigBang will have to be declared abstract.
    final void g() {
    }
    //final    void h(){} //(1): It will fail because BigBang will try to override a final method.
    protected static int i;
    private int j;
}

final class BigBang extends Bang {

    /**
     * Default constructor (having no arguments) is automatically created only
     * if the class does not define any constructors. So as soon as //2 is
     * inserted the default constructor will not be created.
     */
    //BigBang(int n) { m = n; } //(2):It will fail since BigBang will no longer have a default constructor
    // that is used in the main( ) method.
    public static void main(String args[]) {
        Bang mc = new BigBang();
    }

    void h() {
    }
    //void k(){ i++; } //(3)
    //void l(){ j++; } //(4): It will fail since the method will try to access a private member 'j' of the superclass.
    int m;
}
