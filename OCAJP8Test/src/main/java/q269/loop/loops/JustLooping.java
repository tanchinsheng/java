/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q269.loop.loops;

/**
 *
 * What is the result?
 */
public class JustLooping {

    /**
     * The point to note here is that the j in for loop is different from the
     * instance member j. Therefore, j++ occuring in the for loop doesn't affect
     * the while loop. The for loop prints 1 2 3 4 5. The while loop runs for
     * the values 0 to 5 i.e. 6 iterations. Thus, 1 2 3 4 5 is printed 6 times.
     * Note that after the end of the while loop the value of j is 6.
     */
    private int j; // 0

    void showJ() {
        while (j <= 5) {
            for (int j = 1; j <= 5;) {
                System.out.print(j + " ");
                j++;
            }
            System.out.println();
            j++;
        }
    }

    public static void main(String[] args) {
        new JustLooping().showJ();
    }

}
