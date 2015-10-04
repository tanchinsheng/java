/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package q422.inheritance;

/**
 *
 * Given the following classes, what will be the output of compiling and running
 * the class Truck?
 */
class Automobile {

    public void drive() {
        System.out.println("Automobile: drive");
    }
}

public class Truck extends Automobile {

    /**
     * Since Truck is a subclass of Automobile, a = t will be valid at compile
     * time as well runtime. But a cast is needed to make for t = (Truck) a;
     * This will be ok at compile time but if at run time 'a' does not refer to
     * an object of class Truck then a ClassCastException will be thrown. Now,
     * method to be executed is decided at run time and it depends on the actual
     * class of object referred to by the variable. Here, at line 4, variable a
     * refers to an object of class Truck. So Truck's drive() will be called
     * which prints Truck: drive. This is polymorphism in action!
     */
    public void drive() {
        System.out.println("Truck: drive");
    }

    public static void main(String args[]) {
        Automobile a = new Automobile();
        Truck t = new Truck();
        a.drive(); //1
        t.drive(); //2
        a = t;     //3
        a.drive(); //4: Truck: drive
    }

}
