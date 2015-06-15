/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter1;

/**
 *
 * @author cstan
 */
public class Q02_WaterBottle {

    private String brand;
    private boolean empty;

    public static void main(String[] args) {
        Q02_WaterBottle wb = new Q02_WaterBottle();
        System.out.print("Empty = " + wb.empty);
        System.out.print(", Brand = " + wb.brand);
    }
}
