/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q296.loop;

/**
 *
 * What will the following code print when compiled and run?
 */
public class Test {

    /**
     * The point to note here is that a break without any label breaks the
     * innermost outer loop. So in this case, whenever k>j, the C loop breaks.
     * You should run the program and follow it step by step to understand how
     * it progresses.
     */
    public static void main(String args[]) {
        int c = 0;
        A:
        for (int i = 0; i < 2; i++) {
            B:
            for (int j = 0; j < 2; j++) {
                C:
                for (int k = 0; k < 3; k++) {
                    c++;
                    if (k > j) {
                        break;
                    }
                }
            }
        }
        System.out.println(c);
    }

}
