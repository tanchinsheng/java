/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q533.exceptions;

/**
 *
 * What will the following class print ?
 */
public class Test {

    /**
     * If evaluation of a dimension expression completes abruptly, no part of
     * any dimension expression to its right will appear to have been evaluated.
     * Thus, while evaluating a[val()][i=1]++,  val() throws an exception and
     * i=1 will not be executed. Therefore, i remains 99 and a[1][1] will print
     * 11.
     */
    static int val() throws Exception {
        throw new Exception("unimplemented");
    }

    public static void main(String[] args) {
        int[][] a = {{00, 01}, {10, 11}};
        int i = 99;
        try {
            a[val()][i = 1]++;
        } catch (Exception e) {
            System.out.println(i + ", " + a[1][1]);
            System.err.println(i + ", " + a[1][1]);
        }
    }

}
