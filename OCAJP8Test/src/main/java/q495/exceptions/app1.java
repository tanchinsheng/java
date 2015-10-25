/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q495.exceptions;

/**
 *
 * @author cstan
 */
public class app1 {

    /**
     * @param args the command line arguments
     */
    static int factorial(int n) {
        if (n == 1) {
            return 1;
        } else {
            return n * factorial(n - 1);
        }
    }

    public static void main(String[] args) {
        int i = factorial(10000000);
    }

}
