/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q108.javadatatypes;

/**
 *
 * @author cstan
 */
public class Test {

    public static void testInts(Integer obj, int var) {
        obj = var++;
        obj++;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Integer val1 = new Integer(5);
        int val2 = 9;
        testInts(val1++, ++val2);
        System.out.println(val1 + " " + val2);
    }

}
