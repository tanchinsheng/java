/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5.q16;

abstract class Reptile {

    public final void layEggs() {
        System.out.println("Reptile laying eggs");
    }

    public static void main(String[] args) {
        Reptile reptile = new Lizard();
        reptile.layEggs();
    }
}

public class Lizard extends Reptile {

    public void layEggs() {
        System.out.println("Lizard laying eggs");
    }
}
