/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q162.operators.decision;

/**
 *
 * Which line, if any, will give a compile time error ?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    void test(byte x) {
        switch (x) {
            case 'a':   // 1
            case 256:   // 2
            case 0:     // 3
            default:   // 4
            case 80:    // 5
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
