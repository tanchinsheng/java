/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q571.string;

/**
 *
 * What will be the result of attempting to compile and run the following
 * program?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String s = "hello";
        StringBuilder sb = new StringBuilder("hello");
        sb.reverse();
        s.reverse(); //There is no reverse() method in String class.
        if (s == sb.toString()) {
            System.out.println("Equal");
        } else {
            System.out.println("Not Equal");
        }
    }

}
