/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q065.javadatatypes;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * You may use underscore for all kinds of numbers including long, double,
     * float, binary, as well as hex.  For example, the following are all valid
     * numbers - int hex = 0xCAFE_BABE; float f = 9898_7878.333_333f; int bin =
     * 0b1111_0000_1100_1100;
     */
    public static void main(String[] args) {

        int x = 1____3;//1
        long y = 1_3;//2     
        float z = 3.234_567f; //3          
        System.out.println(x + " " + y + " " + z);
        // The number at //1 and //2 are actually the same.
        // Although confusing, it is legal to have multiple underscores between two digits.
    }

}
