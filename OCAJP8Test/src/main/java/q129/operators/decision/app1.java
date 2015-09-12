/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q129.operators.decision;

/**
 *
 * Which code fragments will print the last argument given on the command line
 * to the standard output, and exit without any output or exception stack trace
 * if no arguments are given?
 */
public class app1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        if (args.length != 0) {
            System.out.println(args[args.length - 1]);
        }
    }
}
