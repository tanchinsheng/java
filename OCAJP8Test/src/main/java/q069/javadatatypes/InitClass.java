/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q069.javadatatypes;

/**
 *
 * @author cstan
 */
public class InitClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        InitClass obj = new InitClass(5);
    }
    int m;
    static int i1 = 5;
    static int i2;
    int j = 100;
    int x;

    public InitClass(int m) {
        System.out.println(i1 + "  " + i2 + "   " + x + "  " + j + "  " + m);
    }

    {
        j = 30;
        i2 = 40;
    }  // Instance Initializer

    static {
        i1++;
    }      // Static Initializer

}
