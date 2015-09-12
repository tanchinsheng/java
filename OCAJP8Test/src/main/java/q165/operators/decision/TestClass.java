/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q165.operators.decision;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static int switchTest(int k) {
        int j = 1;
        switch (k) {
            case 1:
                j++;
            case 2:
                j++;
            case 3:
                j++;
            case 4:
                j++;
            case 5:
                j++;
            default:
                j++;
        }
        return j + k;
    }

    public static void main(String[] args) {
        System.out.println(switchTest(4));
    }

}
