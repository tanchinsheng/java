/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q327.methods;

/**
 *
 * What will be the result of attempting to compile and run the following class?
 */
public class InitTest {

    /**
     * @param args the command line arguments
     */
    static String s1 = sM1("a");

    {
        s1 = sM1("b");
    }

    static {
        s1 = sM1("c");
    }

    private static String sM1(String s) {
        System.out.println(s);
        return s;
    }

    public static void main(String[] args) {
        InitTest it = new InitTest();
    }

}
