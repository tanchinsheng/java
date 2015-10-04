/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q415.inheritance;

/**
 *
 * Given the following classes and declarations, which of these statements about
 * //1 and //2 are true?
 */
class A {

    private int i = 10;

    public void f() {
    }

    public void g() {
    }
}

class B extends A {

    public int i = 20;

    public void g() {
    }
}

public class C {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        A a = new A();//1
        A b = new B();//2
        System.out.println(b.i);
        b.f();
    }

}
