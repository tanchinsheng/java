/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q070.javadatatypes;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * Beginning with Java 7, you can include underscores in between the digits.
     * This helps in writing long numbers. For example, if you want to write 1
     * million, you can write: 1_000_000, which is easier to interpret for
     * humans than 1000000. Note that you cannot start or end a value with an
     * underscore though. Thus, 100_ or _100 would be invalid values. _100 would
     * actually be a valid variable name! You may use underscore for all kinds
     * of numbers including long, double, float, binary, as well as hex.  For
     * example, the following are all valid numbers - int hex = 0xCAFE_BABE;
     * float f = 9898_7878.333_333f; int bin = 0b1111_0000_1100_1100;
     */
    public static void main(String[] args) {
        // This is a valid piece of code but the value is not correct.
        // Since it does not start with 0b or 0B, it will NOT be interpreted as a binary number.
        // In fact, since it starts with 0, it will be interpreted as an octal number.
        // int b = 01001110_00100000;

        // If you want to write a value in binary, it must be prefixed with 0b or 0B.
        int b = 0b01001110_00100000;
        System.out.println(b);
    }

}
