/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q072.javadatatypes;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // There no unsigned keyword in java! A char can be used as an unsigned integer.
        unsigned byte b = 0;
        b--;
        System.out.println(b);
    }

}
