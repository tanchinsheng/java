/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q597.string;

/**
 *
 * A String is immutable while a StringBuilder is not. So in stringTest(),
 * "hell".replace('h', 's') will produce a new String "sell" but will not affect
 * the original String that was passed to the method. However, the append()
 * method of StringBuilder appends to the original String object. So, "well"
 * becomes "wello".
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void stringTest(String s) {
        s.replace('h', 's');
    }

    public static void stringBuilderTest(StringBuilder s) {
        s.append("o");
    }

    public static void main(String[] args) {
        String s = "hell";
        StringBuilder sb = new StringBuilder("well");
        stringTest(s);
        stringBuilderTest(sb);
        System.out.println(s + sb); //hellwello
    }
}
