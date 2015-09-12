/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q280.loop;

/**
 *
 * What will the following program print?
 */
public class Test {

    /**
     * In the first iteration of for loop, the while loop keeps running till c
     * becomes 6. Now, for all next for loop iteration, the while loop never
     * runs as the flag is false. So final value of c is 6.
     */
    public static void main(String args[]) {
        int c = 0;
        boolean flag = true;
        for (int i = 0; i < 3; i++) {
            while (flag) {
                c++;
                if (i > c || c > 5) {
                    flag = false;
                }
            }
        }
        System.out.println(c);
    }

}
