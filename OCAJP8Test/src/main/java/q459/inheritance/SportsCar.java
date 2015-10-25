/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q459.inheritance;

/**
 *
 * What will be printed when SportsCar is run?
 */
class Car {

    public int gearRatio = 8;

    public String accelerate() {
        return "Accelerate : Car";
    }
}

public class SportsCar extends Car {

    /**
     * The concept is : variables are hidden and methods are overridden. Method
     * to be executed depends on the class of the actual object the variable is
     * referencing to. Here, c refers to object of class SportsCar so
     * SportsCar's accelerate() is selected.
     */
    public int gearRatio = 9;

    public String accelerate() {
        return "Accelerate : SportsCar";
    }

    public static void main(String[] args) {
        Car c = new SportsCar();
        System.out.println(c.gearRatio + "  " + c.accelerate());
    }

}
