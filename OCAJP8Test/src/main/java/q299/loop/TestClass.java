/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q299.loop;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * --k>0 implies, decrement the value of k and then compare with 0.
     * Therefore, the loop will only execute twice, printing 2 and 1. Had it
     * been k-->0, it would imply, first compare k with 0, and then decrement k.
     * In this case, the loop would execute thrice, printing 2, 1, and 0.
     */
    public static void main(String[] args) {
        int k = 2;
        do {
            System.out.println(k);
        } while (--k > 0);
    }

}
