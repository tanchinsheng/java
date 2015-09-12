/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q124.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Integer i = 10;
        Integer j = 10;
        Integer k = new Integer(10);
        System.out.println(i == j);
        System.out.println(i == k); // false
        Byte b = 1;
        Integer c = 1;
        b == c;
    }
}
