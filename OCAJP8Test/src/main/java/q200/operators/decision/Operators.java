/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q200.operators.decision;

/**
 *
 * @author cstan
 */
public class Operators {

    /**
     * @param args the command line arguments
     */
    public static int operators() {
        int x1 = -4;
        int x2 = x1--;
        int x3 = ++x2;
        if (x2 > x3) {
            --x3;
        } else {
            x1++;
        }
        return x1 + x2 + x3;
    }

    public static void main(String[] args) {
        System.out.println(operators());
    }

}
