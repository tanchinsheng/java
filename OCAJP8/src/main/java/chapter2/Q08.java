/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter2;

/**
 *
 * @author cstan
 */
public class Q08 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean x = true, z = true;
        int y = 20;
        System.out.println(false);
        System.out.println(z = true);
        System.out.println(z = false);
        x = (y != 10) ^ (z = false);
        System.out.println(x + ", " + y + "," + z);
    }

}
