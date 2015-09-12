/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q268.loop;

/**
 *
 * What will be the result of attempting to compile and run the following
 * program?
 */
public class TestClass {

    /**
     * Unlike the 'while(){}' loop, the 'do {} while()' loop executes at least
     * once because the condition is checked after the iteration.
     */
    public static void main(String args[]) {
        boolean b = false;
        int i = 1;
        do {
            i++;
        } while (b = !b);
        System.out.println(i);
        // The loop body is executed twice and the program will print 3. 
    }

}
