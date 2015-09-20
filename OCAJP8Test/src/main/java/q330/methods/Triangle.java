/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q330.methods;

/**
 *
 * Identify the correct statements:
 */
public class Triangle {

    /**
     * @param args the command line arguments
     */
    public int base;
    public int height;
    private static double ANGLE;

    public static double getAngle(); // It will not compile because getAngle() has no body.

    public static void Main(String[] args) {
        System.out.println(getAngle());
    }
    // A class can have a method named Main.
    // Although, since it is not same as main, it will not be considered the standard main method that the
    // JVM can invoke when the program is executed.

}
