/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q112.javadatatypes;

/**
 *
 * @author cstan
 */
public class Test {

    /**
     * Not all short values are valid char values, and neither are all char
     * values valid short values, therefore compiler complains for both the
     * lines 2 and 3. They will require an explicit cast.
     */
    public static void main(String[] args) {
        short s = 10;// 1     
        char c = s;// 2
        s = c;// 3
    }

}
