/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q127.operators.decision;

/**
 *
 * Assuming that a valid integer will be passed in the command line as first
 * argument, which statements regarding the following code are correct?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        int x = Integer.parseInt(args[0]);

        switch (x) {
            case x < 5:
                System.out.println("BIG");
                break;
            case x > 5:
                System.out.println("SMALL");
            default:
                System.out.println("CORRECT");
                break;
        }
    }
}
