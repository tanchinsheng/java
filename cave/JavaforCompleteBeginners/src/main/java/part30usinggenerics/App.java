/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package part30usinggenerics;

/**
This tutorial is on generics; a system for creating “parametrized” classes that allow 
* classes to work with other objects, the type of which you can specify when you 
* instantiate (create objects from) the class. Confused? Hopefully this gentle 
* introduction will help. In this video I’ll show you a simple example of a parametrized 
* class, ArrayList, and we’ll look at how the usage of this class has changed since
* generics were introduced.
 */
import java.util.ArrayList;
import java.util.HashMap;
 
class Animal {
     
}
 
public class App {
 
    public static void main(String[] args) {
         
        /////////////////// Before Java 5 ////////////////////////
        ArrayList list = new ArrayList();
         
        list.add("apple");
        list.add("banana");
        list.add("orange");
         
        String fruit = (String)list.get(1);
         
        System.out.println(fruit);
         
        /////////////// Modern style //////////////////////////////
         
        ArrayList<String> strings = new ArrayList<String>();
         
        strings.add("cat");
        strings.add("dog");
        strings.add("alligator");
         
        String animal = strings.get(1);
         
        System.out.println(animal);
         
         
        ///////////// There can be more than one type argument ////////////////////
         
        HashMap<Integer, String> map = new HashMap<Integer, String>();
         
         
        //////////// Java 7 style /////////////////////////////////
         
        ArrayList<Animal> someList = new ArrayList<>();
    }
 
}