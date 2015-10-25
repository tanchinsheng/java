/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q441.inheritance;

/**
 *
 * What is the result of compiling and running the following code ?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    static int si = 10;

    public TestClass() {
        System.out.println(this);
    }

    public String toString() {
        return "TestClass.si = " + this.si;
    }

    public static void main(String[] args) {
        new TestClass();
    }

}
