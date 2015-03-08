/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package packt;

/**
 *
 * @author cstan
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int arr[] = {1, 2, 3, 4, 5};
//        for (int n = 1; n < 6; n++) {
//            System.out.println(arr[n]);
//        }
        // Exception in thread "main"
        // java.lang.ArrayIndexOutOfBoundsException: 5

        for (int n = 1; n <= 5; n++) {
            System.out.println(arr[n]);
        }
    }

}
