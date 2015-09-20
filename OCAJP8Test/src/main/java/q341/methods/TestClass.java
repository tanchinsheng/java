/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q341.methods;

import java.util.Stack;

/**
 *
 * What will be the contents of s1 and s2 at the time of the println statement
 * in the main method of the following program?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void processStacks(Stack x1, Stack x2) {
        x1.push(new Integer("100")); //assume that the method push adds the passed object to the stack.
        x2 = x1;
    }

    public static void main(String[] args) {
        Stack s1 = new Stack();
        Stack s2 = new Stack();
        processStacks(s1, s2);
        System.out.println(s1 + "    " + s2); // [100]    []
    }

}
