/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q157.operators.decision;

/**
 *
 * @author cstan
 */
public class PromotionTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        int i = 5;
        float f = 5.5f;
        double d = 3.8;
        char c = 'a';
        if (i == f) {
            c++;
        }
        System.out.println((int) f);
        System.out.println((int) d);
        System.out.println(f + d);
        System.out.println((int) (f + d));
        if (((int) (f + d)) == ((int) f + (int) d)) {
            c += 2;
        }
        System.out.println(c);
    }
}
