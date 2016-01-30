/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q518.exceptions;

/**
 *
 * What can be done to get the following code to compile and run? (Assume that
 * the options are independent of each other.)
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public float parseFloat(String s) {
        float f = 0.0f;      // 1
        try {
            f = Float.valueOf(s).floatValue();    // 2
            return f;      // 3
        } catch (NumberFormatException nfe) {
            f = Float.NaN;    // 4
            return f;     // 5
        } finally {
            return f;     // 6
        }
        return f;    // 7
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
