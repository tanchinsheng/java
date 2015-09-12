/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q283.loop;

/**
 *
 * How many times is 2 printed out in the output?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int[] values = {10, 30, 50};
        for (int val : values) {
            int x = 0;
            while (x < values.length) {
                System.out.println(x + " " + val);
                x++;
            }
        }

    }

}
