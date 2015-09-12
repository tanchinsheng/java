/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q267.loop;

/**
 *
 * Which of the following code snippets will compile without any errors?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int x1 = 0;
        while (false) { // unreachable statement
            x1 = 3;
        }

        int x2 = 0;
        if (false) {
            x2 = 3;
        }

        int x3 = 0;
        do {
            x3 = 3;
        } while (false);
        // In a do- while, the block is ALWAYS executed at least once because the condition check
        // is done after the block is executed. Unlike a while loop, where the condition is checked
        // before the execution of the block.

        int x4 = 0;
        for (int i = 0; i < 0; i++) {
            x4 = 3;
        }
    }

}
