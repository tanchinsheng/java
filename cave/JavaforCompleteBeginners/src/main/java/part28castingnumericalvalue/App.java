/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package part28castingnumericalvalue;

/**
A tutorial on casting numerical types; when and why you need casts, 
* and a pitfall to watch out for.
 */
public class App {
 
    /**
     * @param args
     */
    public static void main(String[] args) {
 
        byte byteValue = 20;
        short shortValue = 55;
        int intValue = 888;
        long longValue = 23355;
         
        float floatValue = 8834.8f;
        float floatValue2 = (float)99.3;
        double doubleValue = 32.4;
         
        System.out.println(Byte.MAX_VALUE);
         
        intValue = (int)longValue;
         
        System.out.println(intValue);
         
        doubleValue = intValue;
        System.out.println(doubleValue);
         
        intValue = (int)floatValue;
        System.out.println(intValue);
     
     
        // The following won't work as we expect it to!!
        // 128 is too big for a byte.
        byteValue = (byte)128;
        System.out.println(byteValue);
 
    }
 
}