/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q276.loop;

/**
 *
 * What will be the output if you run the following program?
 */
public class TestClass {

    /**
     * j will be less than 1 for only the first iteration. So, first it will
     * print 0, 0. Next, i and j are incremented.   Because j is not less than 1
     * at the start of the loop, the condition fails and it comes out of the
     * loop. Finally, it will print 1,1.
     */
    public static void main(String args[]) {
        int i;
        int j;
        for (i = 0, j = 0; j < 1; ++j, i++) {
            System.out.println(i + " " + j);
        }
        System.out.println(i + " " + j);
    }
}
