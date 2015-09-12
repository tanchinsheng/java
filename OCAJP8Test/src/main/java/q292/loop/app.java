/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q292.loop;

/**
 *
 * What will the following code print when compiled and run?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int[][] ab = {{1, 2, 3}, {4, 5}};
        for (int i = 0; i < ab.length; i++) {
            for (int j = 0; j < ab[i].length; j++) {
                System.out.print(ab[i][j] + " ");
                if (ab[i][j] == 2) {
                    break;
                }
            }
            continue;
        }
    }
}
