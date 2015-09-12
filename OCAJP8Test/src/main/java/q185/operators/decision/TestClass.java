/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q185.operators.decision;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        boolean flag = true;
        switch (flag) {  // A boolean cannot be used for a switch statement. It needs an integral type, an enum, or a String.
            case true:
                System.out.println("true");
            default:
                System.out.println("false");
        }
    }

}
