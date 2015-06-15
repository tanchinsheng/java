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
public class Q03 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int x = 0;
        while (x++ < 10) {
        }
        String message1 = x > 10 ? "Greater than" : false;
        boolean message2 = x > 10 ? true : false;
        System.out.println(message1 + "," + x);
    }

}
