/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q110.javadatatypes;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * Although 1.0 and 43e1 can fit into a float, the implicit narrowing does
     * not happen because implicit narrowing is permitted only among byte, char,
     * short, and int.
     */
    public static void main(String[] args) {
        float f1 = 1.0;
        float f2 = 43e1;
        float f3 = -1;
        float f4 = 0x0123;
        float f5 = 4;
    }

}
