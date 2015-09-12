/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q239.arrays;

/**
 *
 * What would be the result of trying to compile and run the following program?
 */
public class Test {

    /**
     * Following are the default values that instance variables are initialized
     * with if not initialized explicitly: types (byte, short, char, int, long,
     * float, double) to 0 ( or 0.0 ). All Object types to null. boolean to
     * false.
     */
    int[] ia = new int[1];
    Object oA[] = new Object[1];
    boolean bool;

    public static void main(String[] args) {
        Test test = new Test();
        System.out.println(test.ia[0] + "  " + test.oA[0] + "  " + test.bool); // 0  null  false
    }

}
