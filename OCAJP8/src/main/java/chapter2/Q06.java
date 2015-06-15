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
public class Q06 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int x = 4;
        long y = x * 4 - x++;
        if (y < 10) {
            System.out.println("Too Low");
        } else {
            System.out.println("Just right");
        }
        else System.out.println("Too High");
    }

}
