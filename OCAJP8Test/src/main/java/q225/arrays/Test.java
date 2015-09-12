/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q225.arrays;

/**
 *
 * What will it print when compiled and run ?
 */
public class Test {

    /**
     * In an array access, the expression to the left of the brackets appears to
     * be fully evaluated before any part of the expression within the brackets
     * is evaluated. In the expression a[(a=b)[3]], the expression a is fully
     * evaluated before the expression (a=b)[3]; this means that the original
     * value of a is fetched and remembered while the expression (a=b)[3] is
     * evaluated. This array referenced by the original value of a is then
     * subscripted by a value that is element 3 of another array (possibly the
     * same array) that was referenced by b and is now also referenced by a. So,
     * it is actually a[0] = 1.
     *
     * Note that if evaluation of the expression to the left of the brackets
     * completes abruptly, no part of the expression within the brackets will
     * appear to have been evaluated.
     */
    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4};
        int[] b = {2, 3, 1, 0};
        System.out.println(a[(a = b)[3]]);
    }

}
