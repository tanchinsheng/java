/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q270.loop;

/**
 *
 * What is the result?
 */
public class SimpleLoop {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int i = 0, j = 10;
        while (i <= j) {
            i++;
            j--;
        }
        System.out.println(i + " " + j);
    }

}
