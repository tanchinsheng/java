/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q355.methods.overloading;

/**
 *
 * What would be the output when the above program is compiled and run? (Assume
 * that FileNotFoundException is a subclass of IOException, which in turn is a
 * subclass of Exception)
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public void method(Object o) {
        System.out.println("Object Version");
    }

    public void method(java.io.FileNotFoundException s) {
        System.out.println("java.io.FileNotFoundException Version");
    }

    public void method(java.io.IOException s) {
        System.out.println("IOException Version");
    }

    public static void main(String[] args) {
        TestClass tc = new TestClass();
        tc.method(null);
    }

}
