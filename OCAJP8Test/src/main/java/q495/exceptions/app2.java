/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q495.exceptions;

/**
 *
 * @author cstan
 */
public class app2 {

    /**
     * @param args the command line arguments
     */
    static void printMe(Object[] oa) {
        for (int i = 0; i <= oa.length; i++) {
            System.out.println(oa[i]);
        }
    }

    public static void main(String[] args) {
        printMe(null);
    }

}
