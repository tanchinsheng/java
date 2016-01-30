/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q052.javabasics.oo;

/**
 *
 * @author cstan
 */
public class TestClass {

    public static void main(String[] args) throws Exception {
        Square sq = new Square(10.0);
        sq.area = sq.getSide() * sq.getSide();
        System.out.println(sq.getArea());
    }
}
