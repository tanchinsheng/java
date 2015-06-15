/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter3;

import java.util.Arrays;

/**
 *
 * @author cstan
 */
public class Q22 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int[] random = {6, -4, 12, 0, -10};
        int x = 12;
        int y = Arrays.binarySearch(random, x);
        System.out.println(y);
    }

}
