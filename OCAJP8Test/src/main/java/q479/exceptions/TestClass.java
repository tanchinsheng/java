/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q479.exceptions;

/**
 *
 * What will the following program print when run?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            System.exit(0);
        } finally {
            System.out.println("finally is always executed!");
        }
    }

}
