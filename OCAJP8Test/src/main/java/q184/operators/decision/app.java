/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q184.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int i = 0;
        int j = 1;
        if ((i++ == 0) & (j++ == 2)) {
            i = 12;
        }
        System.out.println(i + " " + j);
    }

}
