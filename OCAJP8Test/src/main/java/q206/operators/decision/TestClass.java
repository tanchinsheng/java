/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q206.operators.decision;

/**
 *
 * What will the following code print when run?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public void switchString(String input) {
        switch (input) {
            case "a":
                System.out.println("apple");
            case "b":
                System.out.println("bat");
                break;
            case "c":
                System.out.println("cat");
            default:
                System.out.println("none");
        }
    }

    public static void main(String[] args) {
        TestClass tc = new TestClass();
        tc.switchString("c");
    }

}
