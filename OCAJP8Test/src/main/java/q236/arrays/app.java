/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q236.arrays;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double daaa[][][] = new double[3][][];
        double d = 100.0;
        double[][] daa = new double[1][1];

        daaa[0] = d; // daaa[0] should be a 2 dimensional array because daaa is a 3 dimensional array.
        daaa[0] = daa;
        daaa[0] = daa[0]; // daaa[0] should be a 2 dimensional array while daa[0] is a one dimensional array.
        daa[1][1] = d;
        //daa[1][1] will cause an ArrayIndexOutofBoundsException because daa's length is only 1 and
        // the indexing starts from 0. To access the first element, you should use daa[0][0].
        daa = daaa[0];
    }

}
