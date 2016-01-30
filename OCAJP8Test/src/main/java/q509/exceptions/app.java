/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q509.exceptions;

/**
 *
 * What will the following code snippet print:
 */
public class app {

    /**
     * f = Float.valueOf("12.3"); executes without any problem. int i =
     * Integer.parseInt(s); throws a NumberFormatException because 12.3 is not
     * an integer. Thus, the catch block prints trouble : 12.3
     */
    public static void main(String[] args) {
        Float f = null;
        try {
            f = Float.valueOf("12.3");
            String s = f.toString();
            int i = Integer.parseInt(s);
            System.out.println("i = " + i);
        } catch (Exception e) {
            System.out.println("trouble : " + f); // trouble : 12.3
        }
    }

}
