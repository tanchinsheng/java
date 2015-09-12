/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q147.operators.decision;

/**
 *
 * @author cstan
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            System.out.print(i == 0 ? args[i] : " " + args[i]);
        }
    }
}
