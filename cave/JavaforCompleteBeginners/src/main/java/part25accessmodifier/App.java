package part25accessmodifier;

/**
Discover the meaning of public, private and protected in the Java programming language. 
* Public, private and protected are confusing for beginners, but once you understand 
* how to use classes, they’re actually quite simple.
 */
import part25accessmodifier.world.Plant;
 
/*
 * private --- only within same class
 * public --- from anywhere
 * protected -- same class, subclass, and same package
 * no modifier -- same package only
 */
 
public class App {
 
    /**
     * @param args
     */
    public static void main(String[] args) {
        Plant plant = new Plant();
        System.out.println(plant.name);       
        System.out.println(plant.ID);
         
        // Won't work --- type is private
        //System.out.println(plant.type);
         
        // size is protected; App is not in the same package as Plant.
        // Won't work
        // System.out.println(plant.size);
         
        // Won't work; App and Plant in different packages, height has package-level visibility.
        //System.out.println(plant.height);
    }
 
}