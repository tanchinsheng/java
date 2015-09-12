/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q172.operators.decision;

/**
 *
 * @author cstan
 */
public class Test {

    /**
     * Since 9 does not match any of the case labels, it is accepted by default
     * block. In this block, the else part is executed, which sets s to the
     * value of s+4, i.e. 9. Since there is a break in the else block, case 7:
     * is not executed.
     */
    public static void main(String args[]) {
        int k = 9, s = 5;
        switch (k) {
            default:
                if (k == 10) {
                    s = s * 2;
                } else {
                    s = s + 4;
                    break;
                }
            case 7:
                s = s + 3;
        }
        System.out.println(s); //9
    }

}
