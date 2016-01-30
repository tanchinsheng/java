/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q535.exceptions;

/**
 *
 * @author cstan
 */
public class Fixed {

    /**
     * @param args the command line arguments
     */
    public void myMethod() throws Exception {
        yourMethod();
    }

    public void yourMethod() throws Exception {
        throw new Exception();
    }

    public static void main(String[] args) {
        TestClass tc = new TestClass();
        tc.myMethod();
    }

}
