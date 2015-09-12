/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q219.arrays;

/**
 *
 * @author cstan
 */
public class Test {

    /**
     * The following class will print 'index = 2' when compiled and run.
     */
    public static int[] getArray() {
        // int i = 1 / 0; // Line 1
        return null;
    }

    public static void main(String[] args) {

        int index = 1;
        try {
            getArray()[index = 2]++; // if Line 1 is there, things in bracket would not be executed.
        } catch (Exception e) {
        }//empty catch
        System.out.println("index = " + index);
    }
}
