/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assessment;

import java.util.function.Predicate;

public class Q17 {

    private static boolean test(Predicate<Integer> p) {
        return p.test(5);
    }

    public static void main(String[] args) {
        System.out.println(test(i -> i == 5));
        System.out.println(test(i -> {i == 5;
        }));
        System.out.println(test((i) -> i == 5));

        System.out.println(test((int i) -> i == 5));
        System.out.println(test((Integer i) -> i == 5));

        System.out.println(test((int i) -> {
            return i == 5;
        }));
        System.out.println(test((Integer i) -> {
            return i == 5;
        }));

        System.out.println(test((i) -> {
            return i == 5;
        }));
    }
}
