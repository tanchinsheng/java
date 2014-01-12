/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inheritance2;

/**
 *
 * @author cstan
 */
public class Inheritance2 {

    public static void main(String args[]) {

        Mammal m = new Mammal();
        Dog d = new Dog();

        System.out.println(m instanceof IAnimal);
        System.out.println(d instanceof Mammal);
        System.out.println(d instanceof IAnimal);
    }
}
