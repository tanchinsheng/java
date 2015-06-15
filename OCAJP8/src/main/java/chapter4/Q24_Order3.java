/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter4;

public class Q24_Order3 {

    final String value1 = "1";
    static String value2 = "2";
    String value3 = "3";

    {
        // CODE SNIPPET 1
        value2 = "e";
        value3 = "f";
    }

    static {
        // CODE SNIPPET 2
        value2 = "h";
    }
}
