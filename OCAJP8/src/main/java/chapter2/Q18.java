/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter2;

/**
 *
 * @author cstan
 */
public class Q18 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int count = 0;
        ROW_LOOP:
        for (int row = 1; row <= 3; row++) {
            for (int col = 1; col <= 2; col++) {
                if (row * col % 2 == 0) {
                    continue ROW_LOOP;
                }
                count++;
            }
        }
        System.out.println(count);
    }

}
