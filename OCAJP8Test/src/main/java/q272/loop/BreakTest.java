/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q272.loop;

/**
 *
 * What will the following code print?
 */
public class BreakTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int i = 0, j = 5;
        lab1:
        for (;; i++) {
            for (;; --j) {
                if (i > j) {
                    break lab1;
                }
            }
        }
        System.out.println(" i = " + i + ", j = " + j);
    }

}
