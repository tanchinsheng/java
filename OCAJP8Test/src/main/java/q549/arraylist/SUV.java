/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q549.arraylist;

import java.util.ArrayList;

/**
 *
 * Which of the following options will fail to compile?
 */
abstract class Vehicle {
}

interface Drivable {
}

class Car extends Vehicle implements Drivable {
}

public class SUV extends Car {

    /**
     * Although generics are not included in this exam, some candidates have
     * reported getting similar questions that incidently touch generic syntax
     * but are not really about generics.
     *
     * This question is based on your understand of is-a relationship. When
     * class A extends or implements B directly or indirectly, you can say that
     * A is-a B. Here, Car directly extends Vehicle and directly implements
     * Drivable. Therefore, a Car is-a Vehicle and a Car is-a Drivable.
     * Similarly, an SUV is-a Car and since Car is-a Vehicle and is-a Drivable,
     * SUV is also a Car and a Drivable.
     *
     * Now, the rule is that if you have a container that is meant to contain A,
     * then you can add anything that is-a A to that container. For example, if
     * you have ArrayList<Car>, you can add a SUV to it because an SUV is-a Car.
     * But if you have ArrayList<SUV>, you cannot add a Car to it because a Car
     * is not an SUV.
     */
    public static void main(String[] args) {
        ArrayList<Vehicle> al1 = new ArrayList<>();
        al1.add(new SUV());//Since an SUV is-a Vehicle, you can add instances of SUV in an ArrayList of Vehicles.

        ArrayList<Drivable> al2 = new ArrayList<>();
        al2.add(new Car());//Since an Car is-a Drivable, you can add instances of Car in an ArrayList of Drivables.

        ArrayList<Drivable> al3 = new ArrayList<>();
        al3.add(new SUV());//Since an SUV is-a Drivable, you can add instances of SUV in an ArrayList of Drivables.

        ArrayList<SUV> al4 = new ArrayList<>();
        al4.add(new Car());//Since a Car is not an SUV, you cannot add instances of Car in an ArrayList of SUVs.

        ArrayList<Vehicle> al5 = new ArrayList<>();
        al5.add(new Car());//Since an Car is-a Vehicle, you can add instances of Car in an ArrayList of Vehicles.
    }

}
