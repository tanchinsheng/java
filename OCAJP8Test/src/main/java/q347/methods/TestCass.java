/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q347.methods;

/**
 *
 * What will the following program print?
 */
public class TestCass {

    /**
     * @param args the command line arguments
     */
    static int someInt = 10;

    public static void changeIt(int a) {
        a = 20;
    }

    public static void main(String[] args) {
        changeIt(someInt);
        System.out.println(someInt);
    }

}
