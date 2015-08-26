/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q023.javabasics;

/**
 *
 * @author cstan
 */
public class Triangle {

    /**
     * b and h are method parameters and are only accessible in the method
     * setBase and setHeight respectively. a is a local variable and is
     * accessible only in updateArea method.
     *
     * base, height, and area are instance level and can be accessed from
     * anywhere within the class where "this" is accessible.
     */
    public int base;
    public int height;
    public double area = 0;

    public Triangle(int pBase, int pHeight) {
        this.base = pBase;
        this.height = pHeight;
        updateArea();
    }

    public void updateArea() {
        double a = base * height / 2;
        area = a;
    }

    public void setBase(int b) {
        base = b;
        updateArea();
    }

    public void setHeight(int h) {
        height = h;
        updateArea();
    }

    public static void main(String[] args) {
        // TODO code application logic here
    }

}
