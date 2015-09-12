/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q164.operators.decision;

/**
 *
 * @author cstan
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int x = 1;
        int y = 2;
        int z = x++;
        int a = --y;
        int b = z--;
        b += ++z;
        int answ1 = x > a ? y > b ? y : b : x > z ? x : z;
        int answ2 = x > a ? (y > b ? y : b) : (x > z ? x : z);
        System.out.println(answ1);
        System.out.println(answ2);
    }

}
