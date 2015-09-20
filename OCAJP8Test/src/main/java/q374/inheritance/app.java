/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q374.inheritance;

/**
 *
 * Which of the following declarations defined in a non-abstract class, is
 * equivalent to the above?
 */
interface I {

    int i = 10;
}

public class app implements I {

    /**
     * Fields in an interface are implicitly public, static and final. Although
     * you can put these words in the interface definition but it is not a good
     * practice to do so.
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

}
