/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5.polymorphism;

public class Lemur extends Primate implements HasTail {

    @Override
    public boolean isTailStriped() {
        return false;
    }
    public int age = 10;

    public static void main(String[] args) {
        Lemur lemur = new Lemur();
        System.out.println(lemur.age);
        HasTail hasTail = lemur;
        System.out.println(hasTail.isTailStriped());
        //System.out.println(hasTail.age); // DOES NOT COMPILE
        Primate primate = lemur;

        System.out.println(primate.hasHair());
        // System.out.println(primate.isTailStriped()); // DOES NOT COMPILE
        //Lemur lemur2 = primate; // DOES NOT COMPILE
        Lemur lemur3 = (Lemur) primate;
        System.out.println(lemur3.age);
    }
}
