/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q532.exceptions;

/**
 *
 * What will it return if the method is called with the input "0.0" ?
 */
public class app {

    /**
     * Finally block will always execute (except when there is a System.exit()
     * in try or catch). And inside the finally block, it is setting f to 10.0.
     * So no matter what the input is, this method will always return 10.0.
     */
    public float parseFloat(String s) {
        float f = 0.0f;
        try {
            f = Float.valueOf(s).floatValue();
            return f;
        } catch (NumberFormatException nfe) {
            f = Float.NaN;
            return f;
        } finally {
            f = 10.0f;
            return f;
        }
    }

    public static void main(String[] args) {
        System.out.println(new app().parseFloat("0.0"));
    }

}
