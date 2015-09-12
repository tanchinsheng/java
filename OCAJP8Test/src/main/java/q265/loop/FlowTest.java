/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q265.loop;

/**
 *
 * What can be inserted in the following code so that it will print exactly 2345
 * when compiled and run?
 */
public class FlowTest {

    /**
     * This is a very simple loop to follow if you know what break and continue
     * do. break breaks the nearest outer loop. Once a break is encountered, no
     * further iterations of that loop will execute. continue simply starts the
     * next iteration of the loop. Once a continue is encountered, rest of the
     * statements within that loop are ignored (not executed ) and the next
     * iteration is started.
     */
    static int[] data = {1, 2, 3, 4, 5};

    public static void main(String[] args) {
        for (int i : data) {
            if (i < 2) {
                //insert code1 here
                continue;
            }
            System.out.print(i);
            if (i == 3) {
                //insert code2 here    
            }
        }
    }

}
