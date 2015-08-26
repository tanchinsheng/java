/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q085.javadatatypes;

/**
 *
 * @author cstan
 */
public class InitTest {

    static int si = 10;
    int i;
    final boolean bool;

    // you cannot put the word instance here. It is not a keyword.
    /*
     instance { bool = true; }
     */
    /* code might not be initialized
     { si = 5; i = bool ? 1000 : 2000;}
     */
    /* code compile
     {
     bool = (si > 5);
     i = 1000;
     }
     */
    // 1
    /**
     * A final variable must be initialized when an instance is constructed, or
     * else the code will not compile. This can be done either in an instance
     * initializer or in EVERY constructor. The keyword static is used to
     * signify that a block is static initializer. If nothing is there before
     * starting curly brace then it is an instance initializer.
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
