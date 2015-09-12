/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q293.loop;

/**
 *
 * What will be the output when the following program is run?
 */
public class TestClass {

    /**
     * ++j and i++ do not matter in this case. The loop will not execute even
     * once since j is not less than i at the start of the loop so the condition
     * fails and the program will print 0 0 just once.
     */
    public static void main(String args[]) {
        int i;
        int j;
        for (i = 0, j = 0; j < i; ++j, i++) {
            System.out.println(i + " " + j);
        }
        System.out.println(i + " " + j);
    }
}
