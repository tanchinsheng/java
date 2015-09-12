/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q141.operators.decision;

/**
 *
 * @author cstan
 */
public class ScopeTest {

    /**
     * x is first initialized by x = 3, then the value of this expression (i.e.
     * "x = 3"), which is 3, is multiplied by 4 and is again assigned to x. So
     * it prints 12.
     */
    static int x = 5;

    public static void main(String[] args) {
        int x = (x = 3) * 4;// 1      
        System.out.println(x);
    }

}
