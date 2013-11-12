/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package part31genericsandwildcards;

/**
A tutorial on using wildcards with generic classes in Java. Want to impress even 
* hardened Java developers with the depth of your Java knowledge? This should do the trick.
* Wildcards are mostly useful when you want to pass parametrized classes to methods and 
* those classes may contain various kinds of subtype or supertype objects.
 */
import java.util.ArrayList;
 
class Machine {
 
    @Override
    public String toString() {
        return "I am a machine";
    }
     
    public void start() {
        System.out.println("Machine starting.");
    }
 
}
 
class Camera extends Machine {
    @Override
    public String toString() {
        return "I am a camera";
    }
     
    public void snap() {
        System.out.println("snap!");
    }
}
 
public class App {
 
    public static void main(String[] args) {
 
        ArrayList<Machine> list1 = new ArrayList<>();
 
        list1.add(new Machine());
        list1.add(new Machine());
 
        ArrayList<Camera> list2 = new ArrayList<>();
 
        list2.add(new Camera());
        list2.add(new Camera());
 
        showList(list2);
        showList2(list1);
        showList3(list1);
    }
 
    public static void showList(ArrayList<? extends Machine> list) {
        for (Machine value : list) {
            System.out.println(value);
            value.start();
        }
 
    }
     
    public static void showList2(ArrayList<? super Camera> list) {
        for (Object value : list) {
            System.out.println(value);
        }
    }
     
    public static void showList3(ArrayList<?> list) {
        for (Object value : list) {
            System.out.println(value);
        }
    }
 
 
}