/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q080.javadatatypes;

/**
 *
 * @author cstan
 */
public class CastTest {

    /**
     * byte and int both hold signed values. So when b is assigned to i, the
     * sign is preserved.
     */
    public static void main(String[] args) {
        byte b = -128;
        int i = b;
        b = (byte) i;
        System.out.println(i + " " + b);
    }

}
