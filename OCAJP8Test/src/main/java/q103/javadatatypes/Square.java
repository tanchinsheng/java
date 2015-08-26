/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q103.javadatatypes;

/**
 *
 * @author cstan
 */
public class Square {

    private double side = 0;
    String color;

    public Square(double length) {
        this.side = length;
    }

    public double getSide() {
        return side;
    }

    public void setSide(double side) {
        this.side = side;
    }

}
