/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q589.string;

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
        StringBuilder sb = new StringBuilder("12345678");
        sb.setLength(5);
        sb.setLength(10);
        System.out.println(sb.length());
        System.out.println(sb);
    }

}
