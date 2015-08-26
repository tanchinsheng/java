/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q058.javadatatypes;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int i1 = 1, i2 = 2, i3 = 3;
        int i4 = i1 + (i2 = i3);
        System.out.println(i4);
    }

}
