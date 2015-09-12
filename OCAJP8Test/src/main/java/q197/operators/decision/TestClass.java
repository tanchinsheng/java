/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q197.operators.decision;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * Remember that the args array is never null. If the program is run without
     * any arguments, args points to a String array of length 0. Therefore,
     * hasParams will be true and it will print "has params". Since there is no
     * else, the subsequent code block will also be executed and it will print
     * "no params". Note that it is not syntactically wrong to have section of
     * code wrapped in { }.
     */
    public static void main(String[] args) {

        boolean hasParams = (args == null ? false : true);
        if (hasParams) {
            System.out.println("has params");
        }
        {
            System.out.println("no params");
        }
    }

}
