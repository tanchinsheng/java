/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q223.arrays;

/**
 *
 * What will it print ?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean[] b1 = new boolean[2];
        boolean[] b2 = {true, false};
        System.out.println("" + (b1[0] == b2[0]) + ", " + (b1[1] == b2[1]));
    }

}
