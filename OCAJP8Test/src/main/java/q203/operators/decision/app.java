/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q203.operators.decision;

/**
 *
 * The following method will compile and run without any problems.
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public void switchTest(byte x) {
        switch (x) {
            case 'b':   // 1
            default:   // 2
            case -2:    // 3
            case 80:    // 4
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
