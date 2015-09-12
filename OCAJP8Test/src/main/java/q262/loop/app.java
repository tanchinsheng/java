/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q262.loop;

/**
 *
 * Given the following code fragment, which of the following lines would be a
 * part of the output?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        outer:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                if (i == j) {
                    continue outer;
                }
                System.out.println("i=" + i + " , j=" + j);
            }
        }
    }

}
