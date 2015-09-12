/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q224.arrays;

/**
 *
 * @author cstan
 */
public class FunWithArgs {

    /**
     * There is no problem with the code. The main method is just overloaded.
     * When it is run, the main method with String[] will be called. This method
     * then calls the main with String[][] with an argument { {"a", "b", "c"} }
     * Thus, args[0][1] refers to "b", which is what is printed.
     */
    public static void main(String[][] args) {
        System.out.println(args[0][1]); // b
    }

    public static void main(String[] args) {
        FunWithArgs fwa = new FunWithArgs();
        String[][] newargs = {args};
        fwa.main(newargs);
    }

}
