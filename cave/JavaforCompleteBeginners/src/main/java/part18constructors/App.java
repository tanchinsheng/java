/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package part18constructors;

/**
A tutorial on constructors in Java; what are they, 
* how to create them, using multiple constructors with different parameters 
* and calling constructors from within other constructors.
 */
class Machine {
    private String name;
    private int code;
     
    public Machine() {
        this("Arnie", 0); // must be first line.
        System.out.println("Constructor running!");
    }
     
    public Machine(String name) {
        this(name, 0); // chaining
        System.out.println("Second constructor running");
        // No longer need following line, since we're using the other constructor above.
        //this.name = name;
    }
     
    public Machine(String name, int code) {      
        System.out.println("Third constructor running");
        this.name = name;
        this.code = code;
    }
}
 
public class App {
    public static void main(String[] args) {
        Machine machine1 = new Machine();
        Machine machine2 = new Machine("Bertie");      
        Machine machine3 = new Machine("Chalky", 7);
        //Third constructor running
        //Constructor running!
        //Third constructor running
        //Second constructor running
        //Third constructor running
    }
 
}