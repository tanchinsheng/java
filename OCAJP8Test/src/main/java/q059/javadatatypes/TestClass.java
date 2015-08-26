/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q059.javadatatypes;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * 2ndArgument is not a valid identifier name because an identifier must not
     * start with a digit (although it can contain a digit.) An identifier may
     * start with and contain the underscore character _.
     */
    static int value = 0; //1

    public static void main(String args[]) //2
    {
        int 2ndArgument = Integer.parseInt(args[2]); //3
        if (true == 2 > 10) //4
        {
            value = -10;
        } else {
            value = 2ndArgument;
        }
        for (; value > 0; value--) {
            System.out.println("A"); //5
        }
    }

}
