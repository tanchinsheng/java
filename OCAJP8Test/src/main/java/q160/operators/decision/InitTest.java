/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q160.operators.decision;

/**
 *
 * @author cstan
 */
public class InitTest {

    /**
     * a += (a =4) is same as a = a + (a=4). First, a's value of 10 is kept
     * aside and (a=4) is evaluated. The statement (a=4) assigns 4 to a and the
     * whole statement returns the value 4. Thus, 10 and 4 are added and
     * assigned back to a. Same logic applies to b = b + (b = 5); as well.
     */
    public static void main(String[] args) {
        int a = 10;
        int b = 20;
        a += (a = 4);
        b = b + (b = 5);
        System.out.println(a + ",  " + b); // 14,25
    }

}
