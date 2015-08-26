/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q090.javadatatypes;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String mStr = "123";
        // 1
        long m0 = new Long(mStr); // Auto unboxing will occur.

        // longValue is a non-static method in Long class.
        // Long.longValue(mStr);
        // Long (or any wrapper class) does not have a no-args constructor, so new Long() is invalid.
        // long mx = (new Long()).parseLong(mStr);
        long m1 = Long.parseLong(mStr);
        System.out.println(m1);
        long m2 = Long.valueOf(mStr);
        System.out.println(m2);
        // Long.valueOf(mStr) returns a Long object containing 123. longValue() on the Long object returns 123.
        long m3 = Long.valueOf(mStr).longValue();
        System.out.println(m3);
    }
}
