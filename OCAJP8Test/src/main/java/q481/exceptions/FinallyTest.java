/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q481.exceptions;

/**
 *
 * Considering the following program, which of the options are true?
 */
public class FinallyTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            if (args.length == 0) {
                return;
            } else {
                throw new Exception("Some Exception");
            }
        } catch (Exception e) {
            System.out.println("Exception in Main");
        } finally {
            System.out.println("The end");
        }
    }

}
