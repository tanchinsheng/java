/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q101.javadatatypes;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int rate = 10;
        int t = 5;
        double amount = 1000.0; // only double
        for (int i = 0; i < t; i++) {
            amount = amount * (1 - rate / 100);
        }
    }

}
