/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q471.inheritance.instancefo;

/**
 *
 * @author cstan
 */
class A implements T1, T2 {
}

class B extends A implements T1 {
}

interface T1 {
}

interface T2 {
}

public class TestClass {

    /**
     * Since A implements both T1 and T2, 1 and 2 are correct. b instanceof A
     * will return true as B is a subclass of A. Note that it is 'A' and not
     * 'a'. ( b instanceof a ) will not compile.
     */
    public static void main(String[] args) {

        A a = new A();
        B b = new B();
    }

}
