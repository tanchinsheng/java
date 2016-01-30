/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q045.javabasics.oo;

/**
 *
 * @author cstan
 */
public class Triangle {

    public int base;
    public int height;
    public double area;

    public Triangle(int base, int height) {
        this.base = base;
        this.height = height;
        updateArea();
    }

    void updateArea() {
        area = base * height / 2;
    }

    public void setBase(int b) {
        base = b;
        updateArea();
    }

    public void setHeight(int h) {
        height = h;
        updateArea();
    }
}
