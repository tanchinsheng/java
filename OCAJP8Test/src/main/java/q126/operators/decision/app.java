/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q126.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    static int mx(int s) {
        for (int i = 0; i < 3; i++) {
            s = s + i;
        }
        return s;
    }

    public static void main(String[] args) {
        int s = 5;
        s += s + mx(s) + ++s;
        System.out.println(s);
    }

}
