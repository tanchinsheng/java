/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q129.operators.decision;

/**
 *
 * @author cstan
 */
public class app5 {

    /**
     * If no argument is given, a String array of length Zero is received in the
     * main method. So, there is no NullPointerException on accessing args even
     * if no argument is given. Indexing in java starts from zero. So the last
     * element will be at args.length-1.
     */
    public static void main(String args[]) {
        try {
            System.out.println(args[args.length - 1]);
        } catch (NullPointerException e) {
        }
    }

}
