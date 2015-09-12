/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q237.arrays;

/**
 *
 * Which of the following statements will correctly create and initialize an
 * array of Strings to non null elements?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String[] sA1 = new String[1] {"aaa"
        }; // Array size cannot be given here as the array is being initialized in the declaration.

        String[] sA2 = new String[]{"aaa"};

        String[] sA3 = new String[1];
        sA3[0] = "aaa";

        String[] sA4 = {new String("aaa")};

        String[] sA5 = {"aaa"};
    }

}
