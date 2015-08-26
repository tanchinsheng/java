/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q074.javadatatypes;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * It prints: -2147483648   -2147483648 This happens because negative
     * integers are stored in 2's complement form (complement the bits and add
     * 1). For example: Integer 1 in binary is  00000000 00000000 00000000
     * 00000001  (32 bits) So -1 in binary would be (complement the bits for 1
     * and add 1) : Step 1 (complement the bits of 1): 11111111 11111111
     * 11111111 11111110   Step 2 (add 1 to step 1): 11111111 11111111 11111111
     * 11111111. Now, let's see what happens in this question: a =
     * Integer.MIN_VALUE = 10000000 00000000 00000000 00000000 To get -a, apply
     * the above two steps: Step 1  (complement the bits): 011111111 11111111
     * 11111111 11111111 Step 2 (add 1) : 10000000 00000000 00000000 00000000 So
     * you got the exact same value that you started with! (Note that you can
     * see the binary form of an integer using Integer.toBinaryString(i)
     * method.)
     */
    public static void main(String[] args) throws Exception {
        int a = Integer.MIN_VALUE;
        System.out.println(Integer.toBinaryString(Integer.MIN_VALUE) + " " + Integer.MIN_VALUE);
        System.out.println(Integer.toBinaryString(Integer.MAX_VALUE) + " " + Integer.MAX_VALUE);

        int b = -a;
        System.out.println(a + "   " + b);
    }
}
