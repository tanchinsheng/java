/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q491.exceptions;

/**
 *
 * @author cstan
 */
public class app1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int[] ia = new int[]{1, 2, 3};
        System.out.println(ia[-1]); //Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: -1
    }

}
