/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter4;

interface Climb {

    boolean isToohigh(int height, int limit);
}

public class Q27_Climber {

    private static void check(Climb climb, int height) {
        if (climb.isToohigh(height, 10)) {
            System.out.println("too high");
        } else {
            System.out.println("ok");
        }
    }

    public static void main(String[] args) {
        check((h, l) -> h.append(l).isEmpty(), 5);
    }

}
