/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q603.string;

/**
 *
 * @author cstan
 */
public class App {

    /**
     * Strings are immutable so doing abc.concat("abc") will create a new string
     * "abc" but will not affect the original string "".
     */
    public static void main(String[] args) {
        String abc = "";
        abc.concat("abc");
        abc.concat("def");
        System.out.print(abc);//It will print empty string (or in other words, nothing).

    }

}
