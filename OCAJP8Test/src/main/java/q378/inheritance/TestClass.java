/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q378.inheritance;

/**
 *
 * What, if anything, is wrong with the following code?
 */
interface T1 {

    int VALUE = 1;

    void m1();
}

interface T2 {

    int VALUE = 2;

    void m1();
}

class TestClass implements T1, T2 {

    /**
     * @param args the command line arguments
     */
    public void m1() {
    }

    public static void main(String[] args) {
        TestClass tc = new TestClass();
        System.out.println(((T1) tc).VALUE);
        ((T2) tc).m1();
        tc.m1();
        System.out.println(tc.VALUE);
    }

}
