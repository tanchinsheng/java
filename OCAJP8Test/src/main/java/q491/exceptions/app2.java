/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q491.exceptions;

/**
 *
 * @author cstan
 */
public class app2 {

    /**
     * java.lang.ExceptionInInitializerError Caused by:
     * java.lang.ArithmeticException: / by zero
     */
    static int k = 0;

    static {
        k = 10 / 0;
    }

    public static void main(String[] args) {

    }

}
