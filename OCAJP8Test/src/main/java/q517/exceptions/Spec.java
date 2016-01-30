/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q517.exceptions;

/**
 *
 * @author cstan
 */
class Point {

    int x, y;
}

class ColoredPoint extends Point {

    int color;
}

public class Spec {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ColoredPoint[] cpa = new ColoredPoint[10];
        Point[] pa = cpa;
        System.out.println(pa[1] == null);
        try {
            pa[0] = new Point();
        } catch (ArrayStoreException e) {
            System.out.println(e);
        }
    }

}
