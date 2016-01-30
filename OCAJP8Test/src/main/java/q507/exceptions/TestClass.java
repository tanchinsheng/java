/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q507.exceptions;

/**
 *
 * What will the following program print?
 */
public class TestClass {

    /**
     * You need a boolean in the 'if' condition. Here, compiler sees that there
     * is no way x/y can produce a boolean so it generates an error at compile
     * time.
     */
    public static void main(String[] args) {
        int x = 1;
        int y = 0;
        if (x / y) {
            System.out.println("Good");
        } else {
            System.out.println("Bad");
        }
    }

}
