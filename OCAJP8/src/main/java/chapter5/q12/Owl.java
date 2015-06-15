/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter5.q12;

interface Nocturnal {

    default boolean isBlind() {
        return true;
    }
}

public class Owl implements Nocturnal {

    @Override
    public boolean isBlind() {
        return false;
    }

    public static void main(String[] args) {
        Nocturnal nocturnal1 = (Nocturnal) new Owl();
        System.out.println(nocturnal1.isBlind());
        Nocturnal nocturnal2 = new Owl();
        System.out.println(nocturnal2.isBlind());

    }
}
