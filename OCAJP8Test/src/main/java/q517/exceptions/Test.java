/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q517.exceptions;

/**
 *
 * Which exact exception class will the following class throw when compiled and
 * run?
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static int m1() throws Exception {
        throw new Exception("Some Exception");
    }

    public static void main(String[] args) throws Exception {
        int[] a = null;
        int i = a[m1()];
    }

}
