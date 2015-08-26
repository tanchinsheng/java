/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q054.javabasics.oo;

/**
 *
 * There can be multiple ways to accomplish this. The exam asks you questions on
 * the similar pattern. The key is that your data variable should be private and
 * the functionality that is to be exposed outside should be public. Further,
 * your setter methods should be coded such that they don't leave the data
 * members inconsistent with each other.
 */
public class Square {

    private double side = 0;
    private double area;

    public Square(double length) {
        this.side = length;
    }

    public double getSide() {
        return side;
    }

//    public void setSide(double side) {
//        this.side = side;
//    }
    public void setSide(double d) {
        this.side = d;
        calculateArea();
    }

    public double getArea() {
        return area;
    }

    private void calculateArea() {
        this.area = this.side * this.side;
    }

}
