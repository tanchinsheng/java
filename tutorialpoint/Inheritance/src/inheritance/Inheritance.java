/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inheritance;

/**
 *
 * @author cstan
 */
public class Inheritance {

    /**
     * @param args the command line arguments
     */
 public static void main(String args[]){

      Animal a = new Animal();
      Mammal m = new Mammal();
      Dog d = new Dog();

      System.out.println(m instanceof Animal);
      System.out.println(d instanceof Mammal);
      System.out.println(d instanceof Animal);
   }
}
