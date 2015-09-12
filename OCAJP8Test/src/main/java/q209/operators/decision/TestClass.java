/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q209.operators.decision;

/**
 *
 * What will happen if you try to compile and run the program?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        calculate(2);
    }

    public static void calculate(int x) {
        String val;
        switch (x) {
            case 2:
            //   break;
            default:
                val = "def";
        }
        System.out.println(val);
    }
}
