/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cstan
 */
public class LinearSearch {

    public static int min(int[] arr) {
        int minimum = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < minimum) {
                minimum = arr[i];
            }
        }
        return minimum;
    }

    public static void main(String[] args) {
        int[] data = new int[]{5, 10, 1, 9, 4, 8, 3, 6, 2, 7};
        int minimum = min(data);
        System.out.println("The minimum is :" + minimum);
    }

}
