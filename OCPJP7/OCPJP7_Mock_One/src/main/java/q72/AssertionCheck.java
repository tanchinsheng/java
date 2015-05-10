/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q72;

class Base {

    public void foo() {
        assert true;
    }
}

class Derived extends Base {

    public void foo() {
        assert true;
    }
}

class AssertionCheck {

    public static void main(String[] args) {
        Base base = new Base();
        try {
            base.foo();
        } catch (Exception e) {
            base = new Derived();
            base.foo();
        }
    }

}
