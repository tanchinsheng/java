/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q177.operators.decision;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static int m1(int i) {
        return ++i;
    }

    public static void main(String[] args) {

        int k = m1(args.length);
        k += 3 + ++k;
        System.out.println(k);
    }

}
