/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q57;

/**
 *
 * @author cstan
 */
public class Test {

    public static void main(String[] args) {
        String quote = "aba*abaa**aabaa***";
        // The limit parameter controls the number of times the pattern is applied and
        // therefore affects the length of the resulting array.
        String[] words = quote.split("a\\**", 10);// "\\**" = 0 or more *
        for (String word : words) {
            System.out.println(word);
        }
    }

}
