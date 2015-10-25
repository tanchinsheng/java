/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q460.inheritance;

/**
 *
 * What will be printed when the following code is compiled and run?
 */
class A {

    public int getCode() {
        return 2;
    }
}

class AA extends A {

    public long getCode() {
        return 3;
    }
}

public class TestClass {

    /**
     * @param args the command line arguments
     */
    public int getCode() {
        return 1;
    }

    public static void main(String[] args) {
        A a = new A();
        A aa = new AA();
        System.out.println(a.getCode() + " " + aa.getCode());
    }

}
