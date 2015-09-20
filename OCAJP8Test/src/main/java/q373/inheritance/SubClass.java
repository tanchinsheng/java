/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q373.inheritance;

/**
 *
 * Given the definitions of I and Klass, complete the definition of SubClass so
 * that it extends from Klass and implements I.
 */
interface I {

    void m1();
}

abstract class Klass {

    void m1() {
    }
;

}

class SubClass extends Klass implements I {

    /**
     * Even though class Klass implements m1(), it does not declare that it
     * implements I. Therefore, for a subclass to 'implement' I, it must have
     * 'implements I' in its declaration. Further, m1() in Klass is not public.
     * So even though Subclass inherits m1() from Klass, it will not be a valid
     * implementation of I because interface methods must be public. Therefore,
     * SubClass must override m1() and make it public.
     */
    public void m1() {
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
