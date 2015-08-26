/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q091.javadatatypes;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * Note that float d = 0 * 1.5f; and float d = 0 * (float)1.5 ; are OK A
     * narrowing primitive conversion may be used if all of the following
     * conditions are satisfied: 1. The expression is a constant expression of
     * type int. 2. The type of the variable is byte, short, or char. 3. The
     * value of the expression (which is known at compile time, because it is a
     * constant expression) is representable in the type of the variable. Note
     * that narrowing conversion does not apply to long or double. So, char ch =
     * 30L; will fail even though 30 is representable in char.
     */
    public static void main(String[] args) {
        short s = 12;
        long g = 012; // 012 is a valid octal number.
        int i = (int) false; // Values of type boolean cannot be converted to any other types.
        float f = -123; // Implicit widening conversion will occur in this case.
        // double cannot be implicitly narrowed to a float even though the value is representable by a float.
        float d1 = 0 * 1.5;
        float d2 = 0 * 1.5f;
        float d3 = 0 * (float) 1.5;
    }

}
