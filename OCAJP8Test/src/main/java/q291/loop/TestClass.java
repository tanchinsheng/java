/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q291.loop;

/**
 *
 * What will the following code print when compiled and run?
 */
public class TestClass {

    /**
     * Observe that the line  if (value > 4) { and the rest of the code in the
     * for loop will not execute in any case. It is therefore unreachable code
     * and the compiler will complain about it.
     */
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6};
        int counter = 0;
        for (int value : arr) {
            if (counter >= 5) {
                break;
            } else {
                continue;
            }
            if (value > 4) {
                arr[counter] = value + 1;
            }
            counter++;
        }
        System.out.println(arr[counter]);
    }

}
