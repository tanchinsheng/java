/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q425.inheritance;

/**
 *
 * Given the following interface definition, which definitions are valid?
 */
interface I1 {

    void setValue(String s);

    String getValue();
}

interface I2 extends I1 {

    void analyse();
}

abstract class B implements I1 {

    int getValue(int i) {
        return 0;
    }
}

class A extends I1 { //Classes do not extend interfaces, they implement interfaces.

    String s;

    void setValue(String val) {
        s = val;
    }

    String getValue() {
        return s;
    }
}

interface I3

    implements I1{
    void perform_work();
} //Interfaces do not implement anything, they can extend multiple interfaces.

public class app {

    /**
     * The getValue(int i) method of class B in option c, is different than the
     * method defined in the interface because their parameters are different.
     * Therefore, this class does not actually implement the method of the
     * interface and that is why it needs to be declared abstract.
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
