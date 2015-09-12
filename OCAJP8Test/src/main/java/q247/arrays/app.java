/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q247.arrays;

/**
 *
 * Which of these array declarations and initializations are NOT legal?
 */
public class app {

    /**
     * If you explicitly specify the members then you can't give the size. So
     * option 2 is wrong. The size of the array is never given during the
     * declaration of an array reference. So option 5 is wrong. The size of an
     * array is always associated with the array instance, not the array
     * reference.
     */
    public static void main(String[] args) {
        int[] i1[] = {{1, 2}, {1}, {}, {1, 2, 3}};
        //int i2[] = new int[2] {1, 2};
        //If you give the elements explicitly you can't give the size. So it should be just int[] { 1, 2 } or just { 1, 2 }
        int i3[][] = new int[][]{{1, 2, 3}, {4, 5, 6}};
        int i4[][] = {{1, 2}, new int[2]};
        // int i5[4] = {1, 2, 3, 4};
        // You cannot specify the size on left hand side .
    }

}
