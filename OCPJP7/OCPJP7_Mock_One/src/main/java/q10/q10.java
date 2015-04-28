/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q10;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

/**
 *
 * @author cstan
 */
public class q10 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //without Static import
        System.out.println("Maximum value of int variable in Java without " + "static import : " + Integer.MAX_VALUE);
        System.out.println("Minimum value of int variable in Java without " + "static import : " + Integer.MIN_VALUE);

        //after static import in Java 5
        System.out.println("Maximum value of int variable using " + "static import : " + MAX_VALUE);
        System.out.println("Minimum value of int variable using" + "static import : " + MIN_VALUE);

    }
}
