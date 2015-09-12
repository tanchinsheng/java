/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q216.arrays;

/**
 *
 * What would be the result of compiling and running the following program?
 */
public class SomeClass {

    /**
     * Elements of Arrays of primitive types are initialized to their default
     * value ( i.e. 0 for integral types, 0.0 for float/double and false for
     * boolean) Elements of Arrays of non-primitive types are initialized to
     * null.
     */
    public static void main(String args[]) {
        int size = 10;
        int[] arr = new int[size]; // Here, all the array elements are initialized to have 0.
        for (int i = 0; i < size; ++i) {
            System.out.println(arr[i]);
            // It correctly will declare and initialize an array of length 10 containing int values initialized to have 0.
        }
    }

}
