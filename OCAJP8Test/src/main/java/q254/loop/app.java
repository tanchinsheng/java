/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q254.loop;

/**
 *
 * Identify valid for constructs... Assume that Math.random() returns a double
 * between 0.0 and 1.0 (not including 1.0).
 */
public class app {

    /**
     * The three parts of a for loop are independent of each other. However,
     * there are certain rules for each part. Please go through section 14.14.1
     * of JLS to understand it fully.
     *
     */
    public static void main(String[] args) {
        for (; Math.random() < 0.5;) {
            System.out.println("true");
        }
        // The second expression in a for loop must return a boolean, which is happening here. So this is valid.

        for (;; Math.random() < 0.5) {
            System.out.println("true");
        }
        // Here, the first part (i.e. the init part) and the second part
        // (i.e. the expression/condition part) part of the for loop are empty.
        // Both are valid. (When the expression/condition part is empty, it is interpreted as true.)
        // The third part (i.e. the update part) of the for loop does not allow every kind of statement.
        //  It allows only the following statements here:
        // Assignment, PreIncrementExpression, PreDecrementExpression, PostIncrementExpression,
        // PostDecrementExpression, MethodInvocation, and ClassInstanceCreationExpression.
        // Thus, Math.random()<0.5 is not valid here, and so this will not compile.

        for (;; Math.random()) {
            System.out.println("true");
        }
        // This is a valid never ending loop that will keep printing true.

        for (;;) {
            Math.random() < .05 ? break : continue;
        }
        // This is an invalid use of ? : operator. Both sides of : should return the same type (excluding void).
        // Here, break and continue do not return anything. However, the following would have been valid:
        // for(;Math.random()<.05? true : false;){  }

        for (;;) {
            if (Math.random() < .05) {
                break;
            }
        }
    }
}
