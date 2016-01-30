/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q535.exceptions;

/**
 *
 * What changes can be done to make the above code compile?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public void myMethod() {
        yourMethod();
    }

    public void yourMethod() {
        throw new Exception();
    }

    public static void main(String[] args) {
        TestClass tc = new TestClass();
        tc.myMethod();
    }

}
