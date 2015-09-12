/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q278.loop;

/**
 *
 * What will be the output when the above code is executed?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        int i = 1, j = 10;
        do {
            if (i++ > --j) {
                continue;
            }
        } while (i < 5);
        System.out.println("i=" + i + " j=" + j);
    }

}
