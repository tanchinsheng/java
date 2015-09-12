/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q298.loop;

/**
 *
 * What will the following code print when compiled and run:
 */
public class TestClass {

    /**
     * In Java, a while or do/while construct takes an expression that returns a
     * boolean. The expression --i is an integer, which is invalid and so the
     * compilation fails. You could change it to: while( --i>0 ){ ... }. In this
     * case, --i<0 is a boolean expression and is valid.
     */
    public static void main(String[] args) {
        int k = 2;
        while (--k) {
            System.out.println(k);
        }
    }

}
