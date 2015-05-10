/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q56;

public class Test {

    public static void main(String[] args) {
        int a = 7, b = 10;
        // A number (integer) sandwiched between a % and a $ symbol is used to
        // reorder the input values
        System.out.printf("no:%2$s and %1$s", a, b);
        System.out.printf("\nno:2$s and 1$s", a, b);
    }

}
