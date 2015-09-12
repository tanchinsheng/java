/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q136.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * 1. All the elements of an array of primitives are automatically
     * initialized by default values, which is 0 for numeric types and false for
     * boolean. Therefore, ia[1] is 0. 2. = is not same as ==. The statement b =
     * c assigns c (whose value is 1) to b. which is then printed.
     */
    public static void main(String[] args) {
        int a = 1;
        int[] ia = new int[10];
        System.out.println(ia[0]);
        int b = ia[a];
        int c = b + a;
        System.out.println(b = c);
    }
}
