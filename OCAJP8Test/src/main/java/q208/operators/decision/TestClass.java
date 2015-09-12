/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q208.operators.decision;

/**
 *
 * Which of the following options would be a valid implementation of tester()
 * method?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    //define tester method here
    public boolean tester1() {
        return false;
    }

    public Boolean tester2() {
        return false;
    }

    public static void main(String[] args) {
        TestClass tc = new TestClass();
        while (tc.tester1()) {
            System.out.println("running...");
        }
        while (tc.tester2()) {
            System.out.println("running...");
        }
    }

}
