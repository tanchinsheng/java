/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q202.operators.decision;

/**
 *
 * What will the following code print when run?
 */
public class TestClass {

    public void switchString(String input) {
        switch (input) {
            case "a":
                System.out.println("apple");
            case "b":
                System.out.println("bat");
                break;
            case "B":
                System.out.println("big bat");
            default:
                System.out.println("none");
        }
    }

    /**
     * The switch statement compares the String object in its expression with
     * the expressions associated with each case label as if it were using the
     * String.equals method; consequently, the comparison of String objects in
     * switch statements is case sensitive. The Java compiler generates
     * generally more efficient bytecode from switch statements that use String
     * objects than from chained if-then-else statements.
     */
    public static void main(String[] args) {
        TestClass tc = new TestClass();
        tc.switchString("B");
    }

}
