/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q030.javabasics;

/**
 *
 * @author cstan
 */
public class Parser {

    /**
     * Because 'i' is defined in try block and so it is not visible in the catch
     * block.
     */
    public static void main(String[] args) {
        try {
            int i = 0;
            i = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Problem in " + i);
        }
    }

}
