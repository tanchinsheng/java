/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q161.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * + is overloaded such that if any one of its two operands is a String then
     * it will convert the other operand to a String and create a new string by
     * concatenating the two. Therefore, in 63+"a" and "a"+63, 63 is converted
     * to "63" and 'b' +"a" and "a"+'b', 'b' is converted to "b". Note that in
     * 'b'+ 63 , 'b' is promoted to an int i.e. 98 giving 161.
     */
    public static void main(String[] args) {
        System.out.println("a" + 'b' + 63); // ab63
        System.out.println("a" + 63); // a63
        System.out.println('b' + new Integer(63)); // 98+63=161
        String s1 = 'b' + 63 + "a"; // 161a
        // Since neither of the operands of + operator is a String, it will not generate a String.
        // However, due to auto-unboxing of 10, it will generate an int value of 73.
        String s2 = 63 + new Integer(10);
    }

}
