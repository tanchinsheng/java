/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q144.operators.decision;

/**
 *
 * @author cstan
 */
public class TestClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Short k = 9;
        Integer i = 9;
        Boolean b = false;
        char c = 'a';
        String str = "123";

        i = (int) k.shortValue(); // You can use *= here but then you can't complete the 4th line.
        str += b; // You can't use =, or *= here. Only += is valid.
        b = !b; //  You can't use anything other than = here.
        c *= i; //  You can only use *= or +=. = is not valid. Further, if you use += here, you can't complete line 2.

    }

}
