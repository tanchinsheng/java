/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q087.javadatatypes;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // This is valid because 320 is below the maximum value that a char can take,
        // which is 2^16 -1. Remember that char can take only positive values.
        char c = 320;
        // 320 is an int and any valid int can be assigned to a float or a double variable without a cast.
        // Note that f1 = 320.0 is not valid as 320.0 would be a double and a double can
        // only be assigned to a double without a cast.
        float f1 = 320;
        float f1_ = 320.0;
        double d = 320;
        // 320 cannot fit into a byte so you must cast it.: byte b = (byte) 320;
        byte b = 320;
        // Since both the operands of / are floats, it will result in a float, which can be assigned to f.
        // If you have, 22.0f/7.0, then it would not compile because 7.0 is a double and so 22.0f/7.0 will
        // return a double, which cannot be assigned to a float.
        float f2 = 22.0f / 7.0f;
    }

}
