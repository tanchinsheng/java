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
public class Q07 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int x = 5;
        System.out.println(x > 2 ? x < 4 ? 10 : 8 : 7);

        System.out.println(x > 2 ? (x < 4 ? 10 : 8) : 7);

    }

}
