/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter4.q20;

public class BirdSeed {

    private int numberBags;
    boolean call;

    public BirdSeed() {
        // LINE 1
        this(2);
        call = false;
        // LINE 2
        //this(2);
    }

    public BirdSeed(int numberBags) {
        this.numberBags = numberBags;
    }

    public static void main(String[] args) {
        BirdSeed seed = new BirdSeed();
        System.out.println(seed.numberBags);
    }
}
