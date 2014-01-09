/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package overriding;

public class Overriding {

    public static void main(String args[]) {
        
        Animal b = new Dog("d1", "c1"); // Animal reference but Dog object
        Animal a = new Animal("a1"); // Animal reference and object
        

        a.move();// runs the method in Animal class
        b.move();//Runs the method in Dog class
  
        //b.bark("in");
    }
}
