/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q124.operators.decision;

/**
 *
 * For what command line arguments will the following program print true?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // 0,-1,127 -> true
        // -256, 256, 0-255 false
        Integer i = Integer.parseInt(args[0]);
        Integer j = i;
        i--;
        i++;
        System.out.println((i == j));
    }

}
