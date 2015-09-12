/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q220.arrays;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //int[][] array2D = new int[][]{{0, 1, 2, 4} {5, 6}};
        // It is missing a comma between 4} and {5.
        // It should be: new int[][] { { 0, 1, 2, 4} , {5, 6}};

        int[][][] array3D = {{0, 1}, {2, 3}, {4, 5}};
        //The right side has only two dimensions while the left has three.
        // It should be: int [] [] [] array3D = { { {0, 1}, {2, 3}, {4, 5} } };

        int[] array2D_[] = new int[2][2];
        array2D_[0][0] = 1;
        array2D_[0][1] = 2;
        array2D_[1][0] = 3;
        // Notice that element [1][1] is not given a value explicitly in the code. It is given a default value of 0 automatically.

        //int[][] array2D = new int[][]{0, 1};
        // The right side has only one dimension while the left has two.
        // It should be: int [] [] array2D = new int[][]{ {0}, {1}};
        //
        int[] arr = {1, 2};
        int[][] arr2 = {arr, {1, 2}, arr};
        int[][][] arr3 = {arr2};

    }

}
