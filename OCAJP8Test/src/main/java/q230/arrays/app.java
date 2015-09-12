/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q230.arrays;

/**
 *
 * Which of these array declarations and instantiations are legal?
 */
public class app {

    /**
     * The [] notation can be placed both before and after the variable name in
     * an array declaration.
     *
     * int[] ia, ba; // here ia and ba both are int arrays.
     *
     * int ia[], ba; //here only ia is int array and ba is an int.
     *
     * Multidimensional arrays are created by creating arrays that can contain
     * references to other arrays .
     */
    public static void main(String[] args) {
        int[] a1[] = new int[5][4];
        //This will create an array of length 5. Each element of this array will be an array of 4 ints.
        int a2[][] = new int[5][4];
        //This will create an array of length 5. Each element of this array will be an array of 4 ints.
        //int a3[][] = new int[][4] ;
        //The statement int[ ][4] will not compile, because the dimensions must be created from left to right.
        int[] a4[] = new int[4][];
        // This will create an array of length 4. Each element of this array will be null.
        // But you can assign an array of ints of any length to any of the elements.
        // For example:
        // a[0] = new int[10];//valid
        // a[1] = new int[4];//valid
        // a[2] = new int[]; //invalid because you must specify the length
        // a[3] = new Object[] //invalid because a[3] can only refer to an array of ints.
        // This shows that while creating a one dimensional array,
        // the length must be specified but while creating multidimensional arrays,
        // the length of the last dimension can be left unspecified.
        // Further, the length of multiple higher dimensions after the first one can also
        // be left unspecified if none of the dimensions are specified after it.
        // So for example, a[][][][] = new int[4][3][3][5]; is same as a[][][][] = new int[4][][][];
        // (Note that the first dimension must be specified.)  Thus,  multidimensional arrays do not have to be symmetrical.
        int[][] a5 = new int[5][4];
        // This will create an array of length 5. Each element of this array will be an array of 4 ints.
    }

}
