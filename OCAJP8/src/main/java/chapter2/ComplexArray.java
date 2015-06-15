/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter2;

import java.util.Arrays;

public class ComplexArray {

    public static void main(String[] args) {
        int[][] myComplexArray = {{5, 2, 13}, {3, 9, 8, 9}, {5, 7, 12, 7}};
        for (int[] mySimpleArray : myComplexArray) {
            System.out.println(mySimpleArray);
            System.out.println(Arrays.toString(mySimpleArray));
        }
        for (int[] mySimpleArray : myComplexArray) {
            for (int i = 0; i < mySimpleArray.length; i++) {
                System.out.print(mySimpleArray[i] + "\t");
            }
            System.out.println();
        }
    }

}
