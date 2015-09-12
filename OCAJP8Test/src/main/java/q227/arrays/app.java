/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q227.arrays;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * There is a subtle difference between: int[] i; and int i[]; although in
     * both the cases, i is an array of integers. The difference is if you
     * declare multiple variables in the same statement such as: int[] i, j; and
     * int i[], j;, j is not of the same type in the two cases. In the first
     * case, j is an array of integers while in the second case, j is just an
     * integer. Therefore, in this question: array1 is an array of int array2,
     * array3, array4, and array5  are arrays of int arrays Therefore, option 1,
     * 2 and 5 are valid.
     */
    public static void main(String[] args) {
        int[] array1, array2[];
        int[][] array3;
        int[] array4[], array5[];
    }

}
