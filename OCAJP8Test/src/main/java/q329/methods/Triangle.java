/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q329.methods;

/**
 *
 * @author cstan
 */
public class Triangle {

    /**
     * @param args the command line arguments
     */
    public int base;
    public int height;
    private final double ANGLE;

    public void setAngle(double a) {
        ANGLE = a;
    }

    public static void main(String[] args) {
        Triangle t = new Triangle();
        t.setAngle(90);
    }

}
