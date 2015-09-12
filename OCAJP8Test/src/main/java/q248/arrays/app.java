/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q248.arrays;

/**
 *
 * What will the following lines of code print?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int[][] twoD = {{1, 2, 3}, {4, 5, 6, 7}, {8, 9, 10}};

        System.out.println(twoD[1].length);
        System.out.println(twoD[2].getClass().isArray());
        System.out.println(twoD[1][2]);
    }

}
