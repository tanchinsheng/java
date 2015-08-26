/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q071.javadatatypes;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * You may use underscore for all kinds of numbers including long, double,
     * float, binary, as well as hex.  For example, the following are all valid
     * numbers - int hex = 0xCAFE_BABE; float f = 9898_7878.333_333f; int bin =
     * 0b1111_0000_1100_1100;
     */
    public static void main(String[] args) {
        // An underscore can only occur in between two digits. So the _ before L is invalid.
        long y = 123_456_L;
        // An underscore can only occur in between two digits.
        // So the _ before 1 is invalid. _123_456L is a valid variable name though.
        // So the following code is valid: int _123_456L = 10; long z = _123_456L;
        long z = _123_456L;
        // An underscore can only occur in between two digits. So the _ before . is invalid.
        float f1 = 123_.345_667F;
        float f2 = 123_345_667F;
    }

}
