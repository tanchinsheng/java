/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q139.operators.decision;

/**
 *
 * The following code snippet will print true.
 */
public class app {

    /**
     * First the value of 'str1' is evaluated (i.e. one). Now, before the method
     * is called, the operands are evaluated, so str1 becomes "two". so
     * "one".equals("two") is false.
     */
    public static void main(String[] args) {
        String str1 = "one";
        String str2 = "two";
        System.out.println(str1.equals(str1 = str2));
    }

}
