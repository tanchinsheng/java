/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q255.loop;

/**
 *
 * What will be the result of attempting to compile and run the following
 * program?
 *
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        int i = 0;
        for (i = 1; i < 5; i++) {
            continue;  //(1)
        }
        for (i = 0;; i++) {
            break;       //(2)
        }
        for (; i < 5 ? false : true;);     //(3)
        // The code will compile without error and will terminate without problems when run.
    }

}
