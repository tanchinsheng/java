/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q238.arrays;

/**
 *
 * What will the following code print when compiled and run?
 */
public class OrderTest {

    /**
     * It will throw a RuntimeException at run time. When you do new String[2],
     * you create a String array of two elements. arr is therefore not null. But
     * each element of the array is not given any value and is therefore null.
     * When you call a method on that element (i.e. str.concat(str+" "+ind); in
     * initData), it will generate a NullPointerException, which is a
     * RuntimeException.
     */
    public void initData(String[] arr) {
        int ind = 0;
        for (String str : arr) {
            str.concat(str + " " + ind);
            ind++;
        }
    }

    public void printData(String[] arr) {
        for (String str : arr) {
            System.out.println(str);
        }
    }

    public static void main(String[] args) {
        OrderTest ot = new OrderTest();
        String[] arr = new String[2];
        ot.initData(arr);
        ot.printData(arr);
    }
}
