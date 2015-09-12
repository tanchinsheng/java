/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q214.arrays;

/**
 *
 * Which of the following statements are valid ?
 */
public class app {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //String[] sa1 = new String[3]{"a", "b", "c"};
        // You cannot specify the length of the array ( i.e. 3, here) if you are using the initializer block while declaring the array.
        String sa2[] = {"a ", " b", "c"};
        //String sa3 = new String[]{"a", "b", "c"};
        // here sa is not declared as array of strings but just as a String.
        String sa4[] = new String[]{"a", "b", "c"};
        //String sa5[] = new String[]{"a" "b" "c"};
        //There are no commas separating the strings.
    }

}
