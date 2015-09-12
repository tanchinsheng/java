/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q158.operators.decision;

/**
 *
 * @author cstan
 */
public class CrazyMath {

    /**
     * % is the modulus operator. It returns the remainder of a division. Thus,
     * dx = x%5 assigns 0 to dx because 5 divides 10 perfectly (no remainder).
     * y/dx therefore throws an ArithmeticException because of division by 0,
     * which is caught by the catch block. In the catch block, "Caught AE" is
     * printed" and then dx is set to 2 and dy becomes 20/2 i.e.10 x = x/dx => x
     * becomes 10/2 i.e. 5 and y = y/dy => becomes 20/10 i.e. 2
     */
    public static void main(String[] args) {
        int x = 10, y = 20;
        int dx, dy;
        try {
            dx = x % 5;
            dy = y / dx;
        } catch (ArithmeticException ae) {
            System.out.println("Caught AE");
            dx = 2;
            dy = y / dx;
        }
        x = x / dx;
        y = y / dy;
        System.out.println(dx + " " + dy);
        System.out.println(x + " " + y);
    }
}
