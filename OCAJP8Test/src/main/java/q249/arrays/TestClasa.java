/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q249.arrays;

/**
 *
 * What will be the result of attempting to compile and run the following class?
 */
public class TestClasa {

    /**
     * Arrays are proper objects (i.e. iArr instanceof Object returns true) and
     * Object references are passed by value (so effectively, it seems as though
     * objects are being passed by reference). So the value of reference of iArr
     * is passed to the method incr(int[] i); This method changes the actual
     * value of the int element at 0.
     */
    public static void incr(int n) {
        n++;
    }

    public static void incr(int[] n) {
        n[0]++;
    }

    public static void main(String[] args) {
        int i = 1;
        int[] iArr = {1};
        incr(i);
        incr(iArr);
        System.out.println("i = " + i + "  iArr[0] = " + iArr[0]); // 1 2
    }

}
