/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q411.inheritance;

/**
 *
 * What will be the output of compiling and running the following program:
 */
interface I1 {

    int VALUE = 1;

    void m1();
}

interface I2 {

    int VALUE = 2;

    void m1();
}

public class TestClass implements I1, I2 {

    /**
     * Having ambiguous fields does not cause any problems but referring to such
     * fields in an ambiguous way will cause a compile time error. So you cannot
     * call : System.out.println(VALUE) as it will be ambiguous.   as there is
     * no ambiguity in referring the field: TestClass tc = new TestClass();
     * System.out.println(( ( I1) tc).VALUE); So, any of the VALUE fields can be
     * accessed by casting.
     */
    public void m1() {
        System.out.println("Hello");
    }

    public static void main(String[] args) {
        TestClass tc = new TestClass();
        ((I1) tc).m1();
        System.out.println(((I1) tc).VALUE);
        System.out.println(VALUE);
    }

}
