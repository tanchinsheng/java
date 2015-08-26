/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q054.javabasics.oo;

/**
 *
 *
 * You are assigned the task of refactoring the Square class to make it better
 * in terms of encapsulation. What changes will you make to this class?
 */
public class TestClass {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        Square sq = new Square(10.0);
        // sq.area = sq.getSide() * sq.getSide();
        System.out.println(sq.getArea());
    }

}
