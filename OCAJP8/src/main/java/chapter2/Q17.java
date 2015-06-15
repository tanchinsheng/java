/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter2;

/**
 *
 * @author cstan
 */
public class Q17 {

    public static void main(String[] args) {
        boolean keepGoing = true;
        int result = 15, i = 10;
        do {
            i--;
            if (i == 8) {
                keepGoing = false;
            }
            result -= 2;
        } while (keepGoing);
        System.out.println(result);
    }

}
