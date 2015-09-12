/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q244.arrays;

/**
 *
 * What can be inserted independently in the above code so that it will compile
 * and run without any error or exception?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // int[][] a = new int[2][];
        // This will instantiate only the first dimension of the array.
        // The elements in the second dimension will be null.
        // In other words, a will be instantiated to two elements but a[0] and a[1]
        // will be null and so a[0][0] (and access to all other such ints) will throw a NullPointerException.

        int[][] a = new int[2][4];
        // This is correct because it will instantiate both the dimensions of the array.
        // i.e. a will be initialized with 2 references to int arrays a[0] and a[1].
        // Further, the arrays pointed to by a[0] and a[1] will also be initialized with size 4.

        // int[][] a = new int[4][2];
        // This will initialize a to an array of size 4 and each element of this array will be
        // initialized to an int array of size 2.
        // Therefore, a[0][2], a[0][3], a[1][2], and a[1][3],  will cause an ArrayIndexOutOfBoundsException to be thrown.
        //
        // int[][] a = new int[2][];         
        // a[0] = new int[2];         
        // a[1] = new int[4];
        // Observe that this creates a jagged array. i.e. the elements in the second dimension
        // of a are not of same length. The first element in the second dimension is only of length 2
        // while the second element is of length 4. Since the given code doesn't need a[0][2] and a[0][3], it is ok.
        //
        // int[][] a = new int[4][];         
        // a[0] = new int[2];         
        // a[1] = new int[2];
        // In this case, a[1][2] and a[1][3] will cause an ArrayIndexOutOfBoundsException to be thrown.
        //INSERT CODE HERE
        a[0][0] = 1;
        a[0][1] = 2;
        a[1][0] = 3;
        a[1][1] = 4;
        a[1][2] = 5;
        a[1][3] = 6;
    }

}
