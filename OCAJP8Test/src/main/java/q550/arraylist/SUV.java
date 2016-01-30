/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q550.arraylist;

import java.util.ArrayList;

/**
 *
 * Which of the following options will compile? Please select 2 options
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
     * Now, the rule is that if you have a container that is known to contain A,
     * then the elements that you take out from it are only known to be of type
     * A. For example, if you have an ArrayList of Cars (ArrayList<Car>) and if
     * you take out an element from it, you know for sure that it will be a Car.
     * It could be also be an SUV but you are not sure about that. Therefore,
     * you cannot assign that element to a variable of type SUV without a cast.
     * But since a Car is-a Vehicle, you can assign that element to a variable
     * of type Vehicle.
     *
     */
    public static void main(String[] args) {
        ArrayList<Vehicle> al1 = new ArrayList<>();
        SUV s = al1.get(0);//Since a Vehicle is not an SUV, you cannot assign an instance of a Vehicle directly
        //to a variable of type SUV without a cast.

        ArrayList<Drivable> al2 = new ArrayList<>();
        Car c1 = al2.get(0);//Since an Drivable is not a Car, you cannot assign an instance of a Drivable
        //directly to a variable of type Car without a cast.

        ArrayList<SUV> al3 = new ArrayList<>();
        Drivable d1 = al3.get(0);//Since an SUV is-a Drivable, you can assign an instance of an SUV
        // to a variable of type Drivable.

        ArrayList<SUV> al4 = new ArrayList<>();
        Car c2 = al4.get(0);//Since an SUV is a Car, you can assign an instance of an SUV to a variable of type Car.

        ArrayList<Vehicle> al5 = new ArrayList<>();
        Drivable d2 = al5.get(0);
        //Since a Vehicle is not a Drivable, you cannot assign an instance of a Vehicle to variable of type
        //Drivable without a cast.

    }

}
