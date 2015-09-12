/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q166.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int i = 1;
        int j = i++;
        if ((i == ++j) | (i++ == j)) {
            i += j;
        }
        System.out.println(i);
    }

}
