/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q260.loop;

/**
 *
 * What will the following code snippet print?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int count = 0, sum = 0;
        do {
            if (count % 3 == 0) {
                continue;
            }
            sum += count;
        } while (count++ < 11);
        System.out.println(sum);
    }

}
