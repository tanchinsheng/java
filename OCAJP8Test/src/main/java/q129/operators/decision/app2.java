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
public class app2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.out.println(args.length);
        try {
            System.out.println(args[args.length - 1]);
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

}
