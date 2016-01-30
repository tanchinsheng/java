/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q064.javadatatypes;

/**
 *
 * The real exam contains a few questions that test you on how to write numbers
 * in binary. You might want to go through Section 3.10.1 and 3.10.2 of Java
 * Language Specification to understand how this works.
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // TODO code application logic here
        // 0x implies the following digits must be interpreted as Hexadecimal digits and b is a valid Hexadecimal digit.
        double x1 = 0xb10_000;
        // A number starting with 0b (or 0B) implies that it is written in binary.
        // Since 10000 can fit into a float, an explicit cast is not required.
        // Note that when you specify the bit pattern using binary or hex,
        // an explicit cast is not required even if the number specified using
        // the bit pattern is larger than what a float can hold.
        float x2 = 0b10_000;
        // Since it starts with 0b, that means you are writing the number in binary digits (i.e. 0 or 1).
        // But 2 is not a valid binary digit.
        float x3 = 0b20_000;
        // This is invalid because the floating point suffices f, F or d, D are used only when
        // using decimal system and not while using binary. However, since f is a valid digit
        //in hexadecimal system, a hex number may end with an f although it will not be interpreted
        //as float but as the digit f. Thus, float x = 0x10_000f; and float x = 10_000f;
        //are valid because they are written in hex and decimal respectively but
        // float x = 0b10_000f;  is invalid because is written in binary.
        // Note that a floating point number cannot be written in Octal.
        // Therefore, float x = 010_000f; is valid but it is not octal even though it starts with a 0.
        // It is interpreted in decimal.
        float x4 = 0b10_000f;
        long x5 = 0b10000L;
        // A floating point number written in binary or hex cannot use any suffix for float.
        // But a floating point number written in decimal can use the floating point suffices f, F, d, and D.
        // Thus float dx = 0xff; is valid but the f here is not for indicating that it is a float
        // but is interpreted as the hex digit F.

        double x6 = 0b10_000D;
        //INSERT CODE HERE
        System.out.println(x1);
    }

}
