/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q320.methods;

/**
 *
 * What would be the result of attempting to compile and run the following
 * program?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    static TestClass ref;
    String[] arguments;

    public void func(String[] args) {
        ref.arguments = args;
    }

    public static void main(String args[]) {
        ref = new TestClass();
        ref.func(args);
    }

}
