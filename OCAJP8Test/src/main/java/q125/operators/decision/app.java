/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q125.operators.decision;

/**
 *
 * The following code snippet will print 'true'.
 */
public class app {

    /**
     * This will not compile because a short VARIABLE can NEVER be assigned to a
     * char without explicit casting. A short CONSTANT can be assigned to a char
     * only if the value fits into a char.
     *
     * short s = 1; byte b = s; => this will also not compile because although
     * value is small enough to be held by a byte but the Right Hand Side i.e. s
     * is a variable and not a constant. final short s = 1; byte b = s; => This
     * is fine because s is a constant and the value fits into a byte. final
     * short s = 200; byte b = s; => This is invalid because although s is a
     * constant but the value does not fit into a byte.
     *
     * Implicit narrowing occurs only for byte, char, short, and int. Remember
     * that it does not occur for long, float, or double. So, this will not
     * compile: int i = 129L;
     */
    public static void main(String[] args) {
        short s1 = Short.MAX_VALUE;
        char c = s1;
        System.out.println(c == Short.MAX_VALUE);

        short s2 = 1;
        byte b1 = s2;

        final short s3 = 1;
        byte b2 = s3;

        final short s4 = 200;
        byte b3 = s4;
    }
}
