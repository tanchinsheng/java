/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q288.loop;

/**
 *
 * What will the following program snippet print?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int i = 0, j = 11;
        do {
            if (i > j) {
                break;
            }
            j--;
        } while (++i < 5);
        System.out.println(i + "  " + j);
    }

}
