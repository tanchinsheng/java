/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chapter4.lambda;

import chapter4.lambda.CheckIfHopper;
import chapter4.lambda.CheckTrait;
import chapter4.lambda.Animal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cstan
 */
public class TraditionalSearch {

    public static void main(String[] args) {
        List<Animal> animals = new ArrayList<>();
        animals.add(new Animal("fish", false, true));
        animals.add(new Animal("kangaroo", true, false));
        animals.add(new Animal("rabbit", true, false));
        animals.add(new Animal("turtle", false, true));
        print(animals, new CheckIfHopper());
        print(animals, a -> a.canSwim());
        print(animals, a -> !a.canSwim());
        print(animals, (Animal a) -> {
            return a.canSwim();
        });
    }

    private static void print(List<Animal> animals, CheckTrait checker) {
        for (Animal animal : animals) {
            if (checker.test(animal)) {
                System.out.println(animal + " ");
            }
        }
        System.out.println();
    }

}
